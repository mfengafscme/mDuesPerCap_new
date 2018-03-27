# This is a bash script that gives you a list of all the files
# in your view which are view-private or checked out.  It is
# useful for finding out what you may have forgotten to check in
# or add.
#
# Making this work in cmd.exe is left as a exercise for the reader.

for dir in . test
do
	cleartool ls -view_only $dir | grep -v \~ | grep -v \.nbattrs
done

for dir in src web dist conf test/src test/web
do
	cleartool ls -recurse -view_only $dir | grep -v \~ | grep -v \.nbattrs
done
