This is version 2 of NuSMV, the New Symbolic Model Verifier.

----------------------------------------------------------------------

NuSMV is a re-implementation and extension of SMV, the first model
checker based on BDDs. It has been designed to be an open architecture
for model checking, which can be reliably used for the verification of
industrial designs, as a core for custom verification tools, and as a
test-bed for formal verification techniques.

NuSMV version 2 extends NuSMV with new model checking algorithms and
techniques. It combines classical BDD-based symbolic techniques with
SAT-based techniques. It also presents other new features: for
instance, it allows for a more powerful manipulation of multiple
models; it can generate flat models for the whole language; it allows
for cone of influence reduction.

The BDD-based model checking component exploits the CUDD library
developed by Fabio Somenzi at Colorado University. The SAT-based model
checking component includes an RBC-based Bounded Model Checker,
connected to the SIM SAT library developed by the University of
Genova. Optionally, NuSMV can also be used with the ZCHAFF SAT 
library developed by the Princeton University and the MINISAT SAT
library developed by Niklas Een or Niklas Sorensson.

NuSMV version 2 is distributed with an OpenSource license, namely the
GNU Lesser General Public License (LGPL). The aim is to provide a
publicly available state-of-the-art symbolic model checker. With the
OpenSource development model, a whole community participates in the
development of a software systems, with a distributed team and
independent peer review. This may result in a rapid system evolution,
and in increased software quality and reliability: for instance, the
OpenSource model has boosted the take-up of notable software systems,
such as Linux and Apache. With the NuSMV OpenSource project, we would
like to reach the same goals within the model checking community,
opening the development of NuSMV.

You can find further details on NuSMV 2 and on the NuSMV project in paper:

  A. Cimatti, E. Clarke, E. Giunchiglia, F. Giunchiglia,
  M. Pistore, M. Roveri, R. Sebastiani, and A. Tacchella.
  "NuSMV 2: An OpenSource Tool for Symbolic Model Checking".
  In Proc. CAV'02, LNCS. Springer Verlag, 2002.


Please contact <nusmv-users@irst.itc.it> for further information on
NuSMV.  Please contact <nusmv@irst.itc.it> for getting in touch with
the NuSMV development staff.


===========
0. CONTENTS
===========
 1. Copyright
 2. Useful links
 3. Building NuSMV
 4. Platforms
 5. Installing NuSMV
 6. Binary distribution
 7. Files in the NuSMV distribution


============
1. COPYRIGHT
============

NuSMV version 2 (NuSMV 2 in short) is licensed under the GNU Lesser
General Public License (LGPL in short). File LGLP-2.1 contains a copy
of the License.

The aim of the NuSMV OpenSource project is to allow the whole model
checking community to participate to the development of NuSMV. To this
purpose, we have chosen a license that:
1) is "copyleft", that is, it requires that anyone who improves the
   system has to make the improvements freely available;
2) permits to use the system in research and commercial applications, 
   without restrictions.  

In brief, the LGPL license allows anyone to freely download, copy,
use, modify, and redistribute NuSMV 2, proviso that any modification
and/or extension to the library is made publicly available under the
terms of LGPL.

The license also allows the usage of the NuSMV 2 as part of a larger
software system *without* being obliged to distributing the whole
software under LGPL. Also in this case, the modification to NuSMV 2
(*not* to the larger software) should be made available under LGPL.

The precise terms and conditions for copying, distribution and
modification can be found in file LGPL-2.1. You can contact
<nusmv-users@irst.itc.it> if you have any doubt or comment on the
license.

*** WARNING *** If your version of NuSMV is linked with ZCHAFF, notice  ***
*** WARNING *** that zChaff is for non-commercial purposes only.        ***
*** WARNING *** NO COMMERCIAL USE OF ZCHAFF IS ALLOWED WITHOUT WRITTEN  ***
*** WARNING *** PERMISSION FROM PRINCETON UNIVERSITY.                   ***
*** WARNING *** Please contact Sharad Malik (malik@ee.princeton.edu)    ***
*** WARNING *** for details.                                            ***

*** WARNING *** If your version of NuSMV is linked with MiniSat, notice ***
*** WARNING *** that MiniSat is for non-commercial purposes only.       ***
*** WARNING *** NO COMMERCIAL USE OF MINISAT IS ALLOWED WITHOUT WRITTEN ***
*** WARNING *** PERMISSION FROM THE AUTHORS OF MINISAT.                 ***
*** WARNING *** Please contact Niklas Een or Niklas Sorensson           ***
*** WARNING *** {een,nik}@cs.chalmers.se for details.                   ***

*** WARNING *** Notice also that the CUDD library is copyright          ***
*** WARNING *** University of Colorado. The CUDD library is             ***
*** WARNING *** NOT covered by LGPL.                                    ***


Different partners have participated the initial release of
NuSMV 2. Every source file in the NuSMV 2 distribution contains a
header that acknowledges the developers and the copyright holders for
the file. In particular:

 * CMU and ITC-IRST contributed the source code on NuSMV version 1;
 * ITC-IRST has also developed several extensions for NuSMV 2;
 * ITC-IRST and the University of Trento have developed
   the SAT-based Bounded Model Checking package on NuSMV 2;
 * the University of Genova has contributed SIM, a state-of-the-art SAT 
   solver, and the RBC package use in the Bounded Model Checking algorithms.

The NuSMV team has also received several contributions for different
part of the system. In particular:

 * Ariel Fuxman <afuxman@cs.toronto.edu> has extended the LTL to SMV 
   tableau translator to the past fragment of LTL
 * Rik Eshuis <eshuis@cs.utwente.nl> has contributed a strong fairness
   model checking algorithm for LTL specifications
 * Dan Sheridan <dan.sheridan@contact.org.uk> has contributed several
   extensions and enhancements to the Bounded Model Checking algorithms.


================
2. USEFUL LINKS
================

The NuSMV home page:

	http://nusmv.irst.itc.it/

The NuSMV mailing list page:

	http://nusmv.irst.itc.it/mail.html

The most recent versions of NuSMV:

	http://nusmv.irst.itc.it/download.html

The CUDD home page: 

	http://vlsi.colorado.edu/~fabio/CUDD/

The SIM home page: 

	http://www.mrg.dist.unige.it/star/sim/

The ZCHAFF home page:

        http://www.princeton.edu/~chaff/zchaff.html

The MiniSat page (Niklas E�n home page):

        http://www.cs.chalmers.se/~een/

The home page of ext, the code documentation tool used in NuSMV:

	http://www.alumni.caltech.edu/~sedwards/ext/

The OpenSource site: 

	http://www.opensource.org/

The GNU General Public License home page:

	http://www.gnu.org/copyleft/	



=================
3. BUILDING NUSMV
=================

To compile NuSMV 2 follows the instructions below.

NOTE: NuSMV is also distributed already compiled for the most common
operating systems and architectures. See the "BINARY DISTRIBUTION"
section for instructions on how to install an already compiled
package.

NOTE: platform specific instructions for building NuSMV are contained
in the "PLATFORMS" section.

To build NuSMV and CUDD as them are distributed, you will need

  * An ANSI C compiler (gcc will do, as will several versions of cc)
  * GNU Flex version 2.5 or greater
  * GNU Bison version 1.22 or greater
  * GNU make utility version 3.74 or greater
  * GNU tar and gzip utilities
  * Approximately 32 MB of free disk space for building the system
  * Approximately 15 MB of free disk space for the installation

A few tools and libraries are required to have full features provided,
but they are not strictly required to build NuSMV. 
In particular:

   * A fully working 'latex' environment for generation of documentation
   * Program 'lynx' or 'links' for generation of online help
   * Library 'expat' for loading of counter-examples
   * Library 'readline' as a usability improvement. 


Furthermore, as a developer you might be interested in modifying the
NuSMV's source packaging structure, for example by adding a new source
file.  In this case a few other tools will be needed:

  * automake (GNU automake) 1.6.3  or greater
  * autoconf (GNU Autoconf) 2.57  or greater
  * aclocal (GNU automake) 1.6.3  or greater
  * autoheader (GNU Autoconf) 2.57  or greater
  * ltmain.sh (GNU libtool) 1.4.3


Basic building steps:

1. Download the most recent versions of NuSMV from the address
   above into a convenient directory, e.g., /tmp.

2. Move to the directory where you would like to build NuSMV and unpack the
   distributions:

	% cd /home/nusmv				# for example
	% gzip -dc /tmp/NuSMV-2.2.x.tar.gz | tar xf -

3. OPTIONAL: ZCHAFF and/or MINISAT SAT solvers can be downloaded and compiled.

   In Bounded Model Checking, the performance of ZCHAFF and MINISAT is
   usually much better than the performance of the SIM SAT solver. 

   For instructions on how to download, compile and link ZCHAFF and 
   MINISAT with NuSMV, read files 'README.zChaff' and 'README.MiniSat',
   respectively.

4. Move into the cudd-2.4.1.0 directory and run make, which will
   compile the CUDD libraries (for 32 bit architectures only):

	% cd cudd-2.4.1.0
	% make 

   For 64 bit architecture use provided Makefile_64bit:
	% cd cudd-2.4.1.0
	% make -f Makefile_64bit

   Note: at the moment, NuSMV 2 requires a modified version of the CUDD
   library. The CUDD library included in the NuSMV distribution must 
   be used with NuSMV even if another version of the CUDD library is
   already installed in the system.

5. Move into the nusmv directory and run configure, which will
   determine some system-specific parameters and create the Makefile:

	% cd ../nusmv
	% ./configure
   
   OPTIONAL: if you want to use ZCHAFF or MINISAT with NuSMV,
   you have to enable them. Read files 'README.zChaff' and
   'README.MiniSat' for more information. 
   ZCHAFF and MINISAT can both be enabled at the same time.

   Note: Not all checks will return "yes."  This is normal and should
   not affect compilation.  However, do be concerned with any warnings
   "configure" produces.

   Note: Occasionally, configure will make a bad guess or will choose
   a default you do not want.  In this case, simply edit the Makefile
   at this point.

   Note: You may want to specify parameters to configure -- see the
   "INSTALLING NUSMV" section below.

6. Build the NuSMV system by running GNU's make utility:

	% make

   This builds an executable "NuSMV" in the current directory.  

   WARNING: If you are not successful in building a NuSMV executable, 
   double check that:
   * you are using Flex version 2.5 or greater (check 
     using "flex -V")
   * you are using GNU Bison version 1.22 or greater (check with 
     "bison -V"). 
   * you are using GNU make utility version 3.74 or greater (check with
     "make -v").

   Having out-of-date versions of these programs can
   lead to obscure compilation and linking errors. You can download
   the new versions from the GNU FTP address above.

   NuSMV expects to find master.nusmvrc file, and a directory
   of help files in the directory given by the environment variable
   NuSMV_LIBRARY_PATH. Set this to the "share" directory in the source
   tree:

	([t]csh) % setenv NuSMV_LIBRARY_PATH $cwd/share
	([ba]sh) % NuSMV_LIBRARY_PATH='pwd'/share; export NuSMV_LIBRARY_PATH

7. OPTIONAL: Verify that NuSMV works by running it on some of the
   examples included in the distribution:

  	% make check

   This step requires less than 3 hours on an Intel Pentium II 300Mhz
   with 512Mb of RAM.


============
4. PLATFORMS
============

NuSMV has been tested in a range of architecture/operating system
combinations. It has been tested on PC Intel, Apple Mac and Sun
architectures, with different versions and distributions of Linux,
Solaris, Windows and Mac OS X.

NuSMV-2.4.2 and later versions can be compiled natively at 64 bits.

File README_PLATFORMS provides detailed information about each
supported platform.


===================
5. INSTALLING NUSMV
===================

Administrators and people who want to discard the source after building
NuSMV will want to install NuSMV in a central area:

* To install the NuSMV executable, library, headers, and help files, type,
  while in the nusmv directory,

	% make install

  By default, this will put binaries, libraries, headers, and help files
  in /usr/local/bin, /usr/local/lib, /usr/local/include, and /usr/local/share
  respectively.  To choose a different location, provide a default prefix
  when you invoke configure, e.g., to install in /opt/nusmv/bin, etc.,
  use

	% ./configure --prefix=/opt/nusmv

  when configuring NuSMV.

  "make clean" removes all the files generated during "make."  This is
  useful when you want to rebuild NuSMV with a different prefix, when you
  want to rebuild with different compiler options, etc.

  The path "/usr/local/share" (or whatever is set via the --prefix option)
  is hard-coded into NUSMV.  In this directory, NuSMV expects to find
  "master.nusmvrc", and the help/ directory.  This may
  be overridden by setting the environment variable "NuSMV_LIBRARY_PATH" to
  the name of an alternate directory, e.g.,

	([t]csh)% setenv NuSMV_LIBRARY_PATH /projects/nusmv/common
	([ba]sh)% NuSMV_LIBRARY_PATH=/projects/nusmv/common; \
	export NuSMV_LIBRARY_PATH 


======================
6. BINARY DISTRIBUTION
======================

NuSMV is also distributed already compiled. To install the binary
distribution of NuSMV follows the instructions below:

1. Download the most recent versions of NuSMV from the addresses
   listed above into a convenient directory, (e.g., /tmp).

2. Move to the root directory where you want to install NuSMV. Let's
   say "/opt" and untar the distribution:

        % cd /opt
        % gzip -dc /tmp/NuSMV-X.Y.Z_Linux-i686.tar.gz | tar xf -

   This command will create a directory "/opt/nusmv-X.Y.Z" 
   containing the NuSMV files (X.Y.Z is the release number).

3. Add the directory /opt/nusmv-X.Y.Z/bin to your command search PATH
   environment variable: 

	([t]csh)% set path=($path /opt/nusmv-X.Y.Z/bin); rehash
	([ba]sh)% PATH=${PATH}:/opt/nusmv-X.Y.Z/bin; export PATH 

4. Set the NuSMV_LIBRARY_PATH environment variable:

 	([t]csh)% setenv NuSMV_LIBRARY_PATH /opt/nusmv-X.Y.Z/share/nusmv
	([ba]sh)% NuSMV_LIBRARY_PATH=/opt/nusmv-X.Y.Z/share/nusmv; \
	export NuSMV_LIBRARY_PATH 

Now you are ready to run and enjoy NuSMV.


==================================
7. FILES IN THE NUSMV DISTRIBUTION
==================================

cudd-2.4.1.0/ The modified CUDD source tree.
	      Further information on CUDD can be found in the
	      README file in this directory.

nusmv/	      The NuSMV source tree.

  AUTHORS           The NuSMV's team members list and other contributors.

  INSTALL           Contains generic installation instructions.

  NEWS		    Contains the changelog for the different releases.

  LGPL-2.1	    The GNU Lesser General Public License.

  Makefile.{am,in}  Templates of Makefile. 

  README	    This file.

  README_PLATFORMS  Platform-related information

  README.zChaff     Contains information about how to embed zChaff into NuSMV.

  README.MiniSat    Contains information about how to embed MiniSat into NuSMV.

  aclocal.m4        Autoconf additional source for generating the configure
                    file. Only useful if you want to modify the
                    configure script.

  configure	    An executable shell script that creates "Makefile" from
		    Makefile.in after determining system-specific parameters.

  configure.ac	    Autoconf source for generating the configure file.
		    Only useful if you want to modify the configure script.

  config.h.in       Template for config.h, the header file containing 
                    configuration information

  nusmv-2.2.pc.in   Template file for nusmv-2.2.pc to be used by pkg-config. 
                    Useful only to link against NuSMV's library. 

  contrib/          A collection of useful programs and scripts.
                    See contrib/README for further information.

  doc/*/*	    Various NuSMV manuals and tutorial. 

  examples/         Various collected examples.

  helpers/          Contains tools, scripts and programs used during
                    configuration, building and installation phases. 

  share/help/       ASCII documentation for each NuSMV command, accessible
                    through the "help" command within NuSMV. Notice that this 
                    is distributed only with binary distributions. 
                    Source distributions when installed throught 'make install' 
                    will generate and install it automatically. 

  share/master.nusmvrc
		    A NuSMV script designed to be ran automatically at start-up:
		    contains aliases for commonly used commands and some default
		    parameter settings.

  src/*  	    Source code packages of the NuSMV system.


===
EOF
===


