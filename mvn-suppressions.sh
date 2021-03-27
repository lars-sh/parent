function suppressGithubChecksumWarnings() {
	cat < /dev/stdin \
	| grep --invert-match --perl-regexp "^Warning:  Checksum validation failed, expected [a-z0-9]+ but is [a-z0-9]+ from github for https://maven.pkg.github.com/lars-sh/parent/de/lars-sh/[^/]+/maven-metadata.xml$"
}

cat < /dev/stdin \
| suppressGithubChecksumWarnings