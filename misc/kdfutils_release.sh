# kdfutils release automation

# adjust to te path of your working copy
#export KDFUTILS_HOME=/mnt/hgfs/sharedDB/db_projects/kdfutils/
export KDFUTILS_HOME=/d/projects/misc/kdfutils

#########################################################################
## Update release notes in [CHANGES.md](../CHANGES.md)


#########################################################################
## Run tests locally

cd $KDFUTILS_HOME || exit 1

./gradlew check

########################################################################
## Increment version in readme, gradle, example-poms and


trim() { while read -r line; do echo "$line"; done; }
kdfutils_version='v'$(grep '^version' ${KDFUTILS_HOME}/build.gradle | cut -f2 -d' ' | tr -d "'" | trim)

echo "new version is $kdfutils_version"



if [[ $kdfutils_version == *"-SNAPSHOT" ]]; then
  echo "ERROR: Won't publish snapshot build $kdfutils_version!" 1>&2
  exit 1
fi

#change to use java8 for building because of https://github.com/Kotlin/dokka/issues/294
# update-java-alternatives -l
#export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64

git config  user.email "holgerbrandl@users.noreply.github.com"

#########################################################################
## Create tag

kscript misc/PatchVersion.kts "${kdfutils_version:1}"

git status
git commit -am "${kdfutils_version} release"


# make sure that are no pending chanes
#(git diff --exit-code && git tag v${kscript_version})  || echo "could not tag current branch"
git diff --exit-code  || echo "There are uncomitted changes"


git tag "${kdfutils_version}"

git push origin
git push origin --tags


########################################################################
### Build and publish the binary release to maven-central

./gradlew publishToMavenLocal

# careful with this one!
# https://getstream.io/blog/publishing-libraries-to-mavencentral-2021/
# https://central.sonatype.org/pages/gradle.html
./gradlew publishToSonatype closeAndReleaseSonatypeStagingRepository
