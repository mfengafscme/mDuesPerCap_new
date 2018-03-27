#!/usr/bin/perl
#This script performes a search/replace on all the files in a given
#directory, recursively.  The given files are also checked out and
#checked into clearcase.
#
# Params:
#  
#  A directory parameter is expected.
#  The $find and $replace string are specified in the script, rather
#  than on the command line, so as to avoid shell escaping issues.

#
# Config
#

#the string to look for
#$find = "t[A-Z]{3}[0-9]{3}_";
$find = "UserPk";

#the string put in place of $find
$replace = "PersonPk";

#the comment use on the clear case check out and check in
#$comment = "Remove tXXX###_ prefix from table names";
$comment = "Removing user_pk, using person_pk instead";

#
# End Config
#


#list of directories to search
my @dirs;
#list of files that mactch the $find string
my @files;


#get a list of all the directores and put them in @dirs
sub getdirs($) {

	my $parent = shift;

	push @dirs, $parent;

	foreach (<$parent/*>) {
		next if /\/\./;
		if (-d) {
			getdirs($_);
		}
	}
}

#check if the given file matches, and is a clearcase files
sub check($) {

	$filename = shift;
	$isFound = 0;

	#see if the file contains the string
	open(FILE, $filename) or die "Could not open $filename";
	foreach (<FILE>) {
		if (/$find/) {
			$isFound = 1;
			last;
		}
	}
	close FILE;

	#check if this is a clearcase file
	if ($isFound) {
		open (STAT, "cleartool ls $filename|") or die "could not get clearcase status of $filename";
		while (<STAT>) {
			if (/Rule:/) {
				print "Adding file: $filename\n";
				push @files, $filename;
				last;
			}
		}
		close STAT;
	}
}

#gets a list of all the files that match
sub getfiles() {
	foreach (@dirs) {
		foreach $filename (<$_/*>) {
			if (not -d $filename) {
				check($filename);
			}
		}
	}
}

#checks out all the files that match
sub checkoutfiles() {
	foreach (@files) {
		print "Checking out $_\n";
		`cleartool co -c "$comment" $_` or warn "Failed";
	}
}

#checks in all the files that match
sub checkinfiles() {
	foreach (@files) {
		print "Checking in $_\n";
		`cleartool ci -c "$comment" $_` or warn "Failed";
	}
}

#performs the replacements and puts the result into temp files
sub maketemps() {
	foreach (@files) {
		open(FILE, $_) or die "Could not open $_";
		open(FILETO, ">$_.replaced") or die "Could not open temp file for $_";
		foreach (<FILE>) {
			s/$find/$replace/g;
			print(FILETO);
		}
		close FILE;
		close FILETO;
	}
}

#removes the temporary files created
sub removetemps() {
	foreach (@files) {
		unlink "$_.replaced";
	}
}


#replaces the files with the temporary files
sub switchfiles() {
	foreach (@files) {
		rename ("$_.replaced", $_) or die "Could not replace file $_";
	}
}


sub main($) {
	getdirs(shift);
	getfiles();
	maketemps();
	checkoutfiles();
	switchfiles();
	#checkinfiles();
	removetemps();
}


main(shift);
