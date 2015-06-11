method SEARCH_FUNCTION .
*&--------------------------------------------------------------------*
*&      Form  search_function
*&--------------------------------------------------------------------*

data :
begin of enfline,
  FUNCNAME type char30,
  AREA type char26,
  ACTIVE type char1,
  GENERATED type char1,
  FREEDATE type char8,
  GLOBAL type  char1,
  LOC_PRIV type  char1,
  EXTEN1 type  char1,
  EXTEN2 type  char1,
  EXTEN3 type  char1,
  EXTEN4 type  char1,
  EXTEN5 type  char1,
end of enfline.

data : begin of tfline,
  FUNCNAME type char30,
  PNAME type char40,
  INCLUDE type char2,
  FREEDATE type char8,
  APPL type char1,
  MAND type  char3,
  FMODE type  char1,
  HOST type  char8,
  UTASK type  char1,
end of tfline.

TYPES: BEGIN OF t_rep_source,
         line(10000),
       END OF t_rep_source.
DATA: l_i_prog_source TYPE TABLE OF t_rep_source ,
      l_was TYPE t_rep_source.

  LOOP AT p_objects INTO line_objects.
    IF line_objects-objtype = 'FUGR'.
      SELECT * FROM enlfdir into enfline WHERE area = line_objects-objname.
        SELECT * FROM tfdir into tfline WHERE funcname = enfline-funcname.
          "pname : nom à recuperer pour les includes
          s_data-object_type = 'FUGR'.
          s_data-object_name = enfline-area.
          s_data-subobject_type = 'FUNC'.
          s_data-subobject_name = tfline-funcname.
          s_data-incl_name = tfline-pname.
          APPEND s_data TO t_data.
        ENDSELECT.
      ENDSELECT.
    ENDIF.
  ENDLOOP.

endmethod.