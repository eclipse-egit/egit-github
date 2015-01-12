#!/bin/sh
# Copyright (C) 2009, Google Inc.
# Copyright (C) 2011, Matthias Sohn <matthias.sohn@sap.com>
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html

# Update all pom.xml with new build number
#
# TODO(spearce) This should be converted to some sort of
# Java based Maven plugin so its fully portable.
#

V=
J=

while [ $# -gt 0 ]
do
case "$1" in
--snapshot=*)
	V=$(echo "$1" | perl -pe 's/^--snapshot=//')
	if [ -z "$V" ]
	then
		echo >&2 "usage: $0 --snapshot=0.n.0"
		exit 1
	fi
	case "$V" in
	*-SNAPSHOT) : ;;
	*) V=$V-SNAPSHOT ;;
	esac
	shift
	;;

--release)
	V=$(git describe HEAD) || exit
	shift
	;;

*)
	echo >&2 "usage: $0 {--snapshot=0.n.0 | --release}"
	exit 1
esac
done

case "$V" in
v*) V=$(echo "$V" | perl -pe s/^v//) ;;
'')
	echo >&2 "usage: $0 {--snapshot=0.n.0 | --release}"
	exit 1
esac

case "$V" in
*-SNAPSHOT)
	POM_V=$V
	OSGI_V="${V%%-SNAPSHOT}.qualifier"
	;;
*-[1-9]*-g[0-9a-f]*)
	POM_V=$(echo "$V" | perl -pe 's/-(\d+-g.*)$/.$1/')
	OSGI_V=$(perl -e '
		$ARGV[0] =~ /^(\d+)(?:\.(\d+)(?:\.(\d+))?)?-(\d+)-g(.*)$/;
		my ($a, $b, $c, $p, $r) = ($1, $2, $3, $4, $5);
		$b = '0' unless defined $b;
		$c = '0' unless defined $c;

		printf "%s.%s.%s.%6.6i_g%s\n", $a, $b, $c, $p, $r;
		' "$V")
	;;
*)
	POM_V=$V
	OSGI_V=$V
	;;
esac

to_version() {
	perl -e '
		$ARGV[0] =~ /^(\d+(?:\.\d+(?:\.\d+)?)?)/;
		print $1
	' "$1"
}

next_version() {
	perl -e '
		$ARGV[0] =~ /^(\d+)(?:\.(\d+)(?:\.(\d+))?)?/;
		my ($a, $b) = ($1, $2);
		$b = 0 unless defined $b;
		$b++;
		print "$a.$b.0";
		' "$1"
}

GITHUB_V=$(to_version "$V")
GITHUB_N=$(next_version "$GITHUB_V")

perl -pi~ -e '
	s/^(Bundle-Version:\s*).*$/${1}'"$OSGI_V"'/;
	s/(org.eclipse.mylyn.internal.github.*;version=")[^"[(]*(")/${1}'"$GITHUB_V"'${2}/;
	s/(org.eclipse.mylyn.internal.github.*;version="\[)[^"]*(\)")/${1}'"$GITHUB_V,$GITHUB_N"'${2}/;
	' $(git ls-files | egrep "META-INF/MANIFEST.MF|META-INF/SOURCE-MANIFEST.MF")

perl -pi~ -e '
	if ($ARGV ne $old_argv) {
		$seen_version = 0;
		$old_argv = $ARGV;
	}
	if (!$seen_version) {
		$seen_version = 1 if (!/<\?xml/ &&
		s/(version=")[^"]*(")/${1}'"$OSGI_V"'${2}/);
	}
	s/(feature="org.eclipse.mylyn.github.core" version=")[^"]*(")/${1}'"$GITHUB_V"'${2}/;
	s/(feature="org.eclipse.mylyn.github.ui" version=")[^"]*(")/${1}'"$GITHUB_V"'${2}/;
	' org.eclipse.mylyn.github-feature/feature.xml

perl -pi~ -e '
	s{<(version)>[^<\$]*</\1>}{<${1}>'"$POM_V"'</${1}>};
	' org.eclipse.mylyn.github-feature/pom.xml

perl -pi~ -e '
	if ($ARGV ne $old_argv) {
		$seen_version = 0;
		$old_argv = $ARGV;
	}
	if (!$seen_version) {
		$seen_version = 1 if
		s{<(version)>[^<\$]*</\1>}{<${1}>'"$POM_V"'</${1}>};
	}
	s{<(egit-version)>[^<\$]*</\1>}{<${1}>'"$POM_V"'</${1}>};
	' pom.xml

perl -pi~ -e '
	if ($ARGV ne $old_argv) {
		$seen_version = 0;
		$old_argv = $ARGV;
	}
	if ($seen_version < 1) {
		$seen_version++ if
		s{<(version)>[^<\$]*</\1>}{<${1}>'"$POM_V"'</${1}>};
	}
	' org.eclipse.mylyn.github-site/pom.xml

perl -pi~ -e '
	if ($ARGV ne $old_argv) {
		$seen_version = 0;
		$old_argv = $ARGV;
	}
	if (!$seen_version) {
		$seen_version = 1 if
		s{<(version)>[^<\$]*</\1>}{<${1}>'"$POM_V"'</${1}>};
	}
	' $(git ls-files | grep pom.*.xml)

find . -name '*~' | xargs rm -f
git diff
