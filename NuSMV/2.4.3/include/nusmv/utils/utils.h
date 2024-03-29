/**CHeaderFile*****************************************************************

  FileName    [utils.h]

  PackageName [utils]

  Synopsis    [External header of the utils package]

  Description [External header of the utils package.]

  SeeAlso     []

  Author      [Marco Roveri]

  Copyright   [
  This file is part of the ``utils'' package of NuSMV version 2. 
  Copyright (C) 1998-2001 by CMU and ITC-irst. 

  NuSMV version 2 is free software; you can redistribute it and/or 
  modify it under the terms of the GNU Lesser General Public 
  License as published by the Free Software Foundation; either 
  version 2 of the License, or (at your option) any later version.

  NuSMV version 2 is distributed in the hope that it will be useful, 
  but WITHOUT ANY WARRANTY; without even the implied warranty of 
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU 
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public 
  License along with this library; if not, write to the Free Software 
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.

  For more information on NuSMV see <http://nusmv.irst.itc.it>
  or email to <nusmv-users@irst.itc.it>.
  Please report bugs to <nusmv-users@irst.itc.it>.

  To contact the NuSMV development board, email to <nusmv@irst.itc.it>. ]

  Revision    [$Id: utils.h,v 1.18.4.8.4.2.6.3 2007/05/08 11:19:30 nusmv Exp $]

******************************************************************************/

#ifndef _UTILS_H
#define _UTILS_H

#if HAVE_CONFIG_H
#include "config.h"
#endif

#include "util.h"

#include "utils/defs.h"
#include "utils/list.h"


/* --------------------------------------------------------------------- */
/*      Exported functions                                               */
/* --------------------------------------------------------------------- */

EXTERN void Utils_FreeListOfLists ARGS((lsList list_of_lists));

EXTERN const char* Utils_StripPath ARGS((const char* pathfname));

EXTERN void 
Utils_StripPathNoExtension ARGS((const char* fpathname, char* filename));

EXTERN char* Utils_get_temp_filename ARGS((const char* templ));
EXTERN char* Utils_get_temp_filename_in_dir ARGS((const char* dir,
						  const char* templ));

EXTERN boolean Utils_file_exists_in_paths ARGS((const char* filename,
						const char* paths,
						const char* delimiters));

EXTERN boolean Utils_file_exists_in_directory ARGS((const char* filename,
						    char* directory));

EXTERN int Utils_strcasecmp ARGS((const char* s1, const char* s2));

#endif /* _UTILS_H */




