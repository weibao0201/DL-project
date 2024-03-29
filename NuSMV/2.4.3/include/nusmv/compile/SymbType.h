/**CHeaderFile*****************************************************************

  FileName    [SymbType.h]

  PackageName [compile.symb_table]

  Synopsis [An interface to deal with the types of variables (during
  compilation and type checking.]

  Description [This class represent the types of the NuSMV type system.
  The types can be:
    a) enum (enumeration), that represents a set of particular values, e.g.
       . a boolean enumeration type: {0,1}
       . a pure symbolic enumeration type: {A, OK, CORRECT, FAIL}
       . a pure integer enumeration type: {1,2,3,4,5, 10, -1, 0}
       . an integer and symbolic enumeration type: {0, 2, 4, OK, FAIL}
    b) integer, that represents the infinite-precision integer arithmetic.
    c) real, that represents the infinite-precision rational arithmetic.
    d) word of width N (N is an integer positive number), that represents
       bit vectors of N bits.
    e) WordArray of address word of width M and value word of width N
       (with M and N integer positive numbers), that represents a
       memory array of 2^M locations, each of which contains a word of
       M bits.  
    f) Set types created by "union" expressions and used in "in", "case"
        and ":=" expressions:
       . a set of boolean values :  0 union 1
       . a set of integer values :  0 union -1 union 10
       . a set of symbolic values :  OK union FAIL
       . a set of integer and symbolic values: OK union 1
       Note that only expressions (not declared variables) can
       have these types.
    g) No-type is an artificial type to represent expressions which usually 
       do not have any type (for example, assignments).
    h) Error type is an artificial type to represent erroneous
       situations.


  A type can be created with the class' constructor. In the case of a
  enum type, during construction it is necessary to specify explicitly the
  list of values this type consists of.

  The constructor is typically used in a symbol table.

  Another possibility is to obtain the types with
  SymbTablePkg_..._type functions. In this case, the enum types will be
  "abstract", i.e. they will consist of some artificial (not existing)
  values. The important feature is that the memory is shared by these
  functions, i.e. you can compare pointers to types, instead of the
  types' contents.  These functions are mostly used in type checking
  (since the particular values of enum types are of no importance).  ]
                                               
  SeeAlso     []

  Author      [Andrei Tchaltsev]

  Copyright   [
  This file is part of the ``compile.symb_table'' package of NuSMV
  version 2.  Copyright (C) 2005 by ITC-irst.

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

  Revision    [$Id: SymbType.h,v 1.1.2.16.6.3 2007/04/20 12:41:29 nusmv Exp $]

******************************************************************************/

#ifndef __SYMB_TYPE_H__
#define __SYMB_TYPE_H__

#include <stdio.h> 

#include "node/node.h"
#include "utils/utils.h"


/**Type***********************************************************************

  Synopsis    [Generic and symbolic encoding]

  Description []

******************************************************************************/
typedef struct SymbType_TAG* SymbType_ptr;

#define SYMB_TYPE(x)  \
        ((SymbType_ptr) x)

#define SYMB_TYPE_CHECK_INSTANCE(x)  \
        (nusmv_assert(SYMB_TYPE(x) != SYMB_TYPE(NULL)))


/**Type***********************************************************************

  Synopsis    [Generic and symbolic encoding]

  Description []

******************************************************************************/
#define nullType   SYMB_TYPE(NULL)


/**Type***********************************************************************

  Synopsis [Possible kinds of a type]

  Description [
  The tags of types that a variable or an expression can have.
  Note that a variable cannot have a set type.]

******************************************************************************/
typedef enum SymbTypeTag_TAG {
  SYMB_TYPE_NONE, /* no-type and no error. */ 
  SYMB_TYPE_STATEMENT, /* for statements like assignements, INIT, TRANS etc */
  SYMB_TYPE_ENUM,  /* an enumeration of values (includes ranges, i.e. -1..4) */
  SYMB_TYPE_INTEGER,  /* (infinite-precision) integer */ 
  SYMB_TYPE_REAL, /* (infinite-precision) rational */
  SYMB_TYPE_WORD, /* word is like an arrary of booleans + arithmetic */
  SYMB_TYPE_WORDARRAY, /* an array of words */
  SYMB_TYPE_SET_BOOL,  /* a set of boolean values */
  SYMB_TYPE_SET_INT,  /* a set of integer values */
  SYMB_TYPE_SET_SYMB, /* a set of symbolic values */
  SYMB_TYPE_SET_INT_SYMB, /* a set of symbolic and integer values */
  SYMB_TYPE_ERROR, /* indicates an error */
  /* SYMB_TYPE_NONE must be the first and SYMB_TYPE_ERROR must be
     the last in the list
  */
} SymbTypeTag;


/* ---------------------------------------------------------------------- */
/*     Public methods                                                     */
/* ---------------------------------------------------------------------- */

EXTERN SymbType_ptr SymbType_create ARGS((SymbTypeTag tag, node_ptr body));
EXTERN void SymbType_destroy ARGS((SymbType_ptr self));
EXTERN SymbType_ptr SymbType_copy ARGS((SymbType_ptr self));

EXTERN SymbTypeTag SymbType_get_tag ARGS((SymbType_ptr self));

EXTERN void SymbType_print ARGS((FILE* output_stream, SymbType_ptr type));

/* a enum of useful auxiliary functions */
EXTERN boolean SymbType_is_boolean_enum ARGS((SymbType_ptr self));
EXTERN boolean SymbType_is_pure_int_enum ARGS((SymbType_ptr self));
EXTERN boolean SymbType_is_pure_symbolic_enum ARGS((SymbType_ptr self));
EXTERN boolean SymbType_is_int_symbolic_enum ARGS((SymbType_ptr self));
EXTERN boolean SymbType_is_word_1 ARGS((SymbType_ptr self));
EXTERN boolean SymbType_is_word ARGS((SymbType_ptr self));
EXTERN boolean SymbType_is_set ARGS((SymbType_ptr self));
EXTERN boolean SymbType_is_error ARGS((SymbType_ptr self));
EXTERN boolean SymbType_is_statement ARGS((SymbType_ptr self));

EXTERN node_ptr SymbType_generate_all_word_values ARGS((SymbType_ptr self));
EXTERN int SymbType_get_word_width ARGS((SymbType_ptr self));
EXTERN int SymbType_get_word_line_number ARGS((SymbType_ptr self));

EXTERN int SymbType_get_wordarray_awidth ARGS((SymbType_ptr self));
EXTERN int SymbType_get_wordarray_vwidth ARGS((SymbType_ptr self));

EXTERN node_ptr SymbType_get_enum_type_values ARGS((SymbType_ptr self));

EXTERN boolean SymbType_is_back_comp ARGS((SymbType_ptr type));

EXTERN SymbType_ptr 
SymbType_get_greater ARGS((SymbType_ptr type1, SymbType_ptr type2));

EXTERN SymbType_ptr 
SymbType_convert_right_to_left ARGS((SymbType_ptr leftType, 
                                     SymbType_ptr rightType));

EXTERN SymbType_ptr 
SymbType_get_minimal_common ARGS((SymbType_ptr type1, SymbType_ptr type2));

EXTERN SymbType_ptr SymbType_make_set_type ARGS((SymbType_ptr type));

EXTERN SymbType_ptr SymbType_make_from_set_type ARGS((SymbType_ptr type));
EXTERN SymbType_ptr SymbType_make_memory_shared ARGS((SymbType_ptr type));

#endif /* __SYMB_TYPE_H__ */
