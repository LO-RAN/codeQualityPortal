METHOD ZCPWR_ANALYSE_SCI_DATA .

  DATA:   p_objects TYPE scit_objs,
          line_objects TYPE scir_objs,
          s_results TYPE scir_rest,
          files TYPE TABLE OF tadir,
          line LIKE LINE OF files,
          name(40),
          nommeth(40).
  TYPES: BEGIN OF tmethod,
            cmpname TYPE char30,
            descript TYPE char30,
            exposure TYPE int4,
            methodkey TYPE string,
          END OF tmethod.
  DATA: dumimethods TYPE STANDARD TABLE OF tmethod.
  DATA:ilocmethods LIKE dumimethods[].
  DATA: locmethodkey TYPE program.
  DATA: methodid TYPE seocpdkey.
  DATA: imethods TYPE STANDARD TABLE OF tmethod.
  DATA: classname TYPE char30.
  DATA : amethod TYPE tmethod.

  LOOP AT t_data INTO s_data.
    line_objects-objtype    = s_data-object_type.
    line_objects-objname    = s_data-object_name.
    line_objects-responsibl = ''.
    line_objects-devclass   = ''.
    line_objects-prgname    = ''.
    INSERT line_objects INTO TABLE p_objects.
  ENDLOOP.

  CALL FUNCTION 'SCI_INSPECT_LIST'
    EXPORTING
      p_user                  = sy-uname
      p_objects               = p_objects
      p_name                  = 'DEFAULT'
    IMPORTING
      p_results               = p_results
    EXCEPTIONS
      no_object               = 1
      objs_already_exists     = 2
      no_default_checkvariant = 3
      too_many_objects        = 4
      could_not_read_variant  = 5
      OTHERS                  = 6.
  IF p_results IS INITIAL.

  ENDIF.

  DATA : clsn TYPE seoclsname.
  TYPES : BEGIN OF seop_method_w_include,
  cpdkey TYPE seocpdkey,
  incname TYPE programm,
  END OF seop_method_w_include.
  DATA methodline TYPE seop_method_w_include.
  DATA  seop_methods_w_include TYPE TABLE OF seop_method_w_include .

  LOOP AT p_results INTO s_results.
    DATA rulename TYPE char30.
    IF s_results-objname <> s_results-sobjname.
      LOOP AT t_data INTO s_data.
        IF s_results-objname = s_data-object_name
          AND s_results-sobjname = s_data-subobject_name.
          write( 'ok : ' ).write( s_results-objname ).write( s_results-sobjname ). write( '<br>' ).
        ENDIF.
      ENDLOOP.
    ENDIF.
    IF NOT s_results-code CS 'MESSAGEG'.
      CONCATENATE s_results-test '_' s_results-code INTO name.
      nommeth = ''.
      IF s_results-objtype EQ 'CLAS'.
        clsn = s_results-objname.
        nommeth = s_results-sobjname.
        LOOP AT t_data INTO s_data.
          IF s_data-object_name = s_results-objname AND s_data-incl_name = s_results-sobjname.
            nommeth = s_data-subobject_name.
            EXIT.
          ENDIF.
        ENDLOOP.
      ENDIF.
      s_results-test = name.
      s_results-param1 = nommeth.
      APPEND s_results TO analysis.
    ENDIF.
  ENDLOOP.
  DATA found TYPE c.
  found = ''.
  LOOP AT t_data INTO s_data.
    found = ''.
    LOOP AT analysis INTO s_results.
      IF s_data-object_name EQ s_results-objname.
        found = 'X'.
      ENDIF.
    ENDLOOP.
    IF found EQ ''.
      CLEAR s_results.
      s_results-objname = s_data-object_name.
      s_results-objtype = s_data-object_type.
      s_results-sobjname = s_data-subobject_name.
      s_results-sobjtype = s_data-subobject_type.
      APPEND s_results TO analysis.
    ENDIF.
  ENDLOOP.
ENDMETHOD.