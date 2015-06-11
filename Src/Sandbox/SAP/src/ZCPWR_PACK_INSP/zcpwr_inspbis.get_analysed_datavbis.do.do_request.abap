METHOD DO_REQUEST.

  obj_name = ''.
  lt_tab = request->get_form_field( 'pkg' ).
  TYPES: BEGIN OF t_rep_source,
           line(10000),
         END OF t_rep_source.
  DATA: l_i_prog_source TYPE TABLE OF t_rep_source ,
        l_was TYPE t_rep_source.
  DATA:   "p_objects TYPE scit_objs,
    line_objects TYPE scir_objs,
    files TYPE TABLE OF tadir,
    line LIKE LINE OF files.
  TYPES: BEGIN OF tmethod,
      cmpname TYPE char30,
      descript TYPE char30,
      exposure TYPE int4,
      methodkey TYPE string,
    END OF tmethod.
  DATA : clsn TYPE seoclsname.
  TYPES : BEGIN OF seop_method_w_include,
            cpdkey TYPE seocpdkey,
            incname TYPE programm,
          END OF seop_method_w_include.
  DATA methodline TYPE seop_method_w_include.
  DATA  seop_methods_w_include TYPE TABLE OF seop_method_w_include .

  DATA: myview TYPE REF TO if_bsp_page.

  myview = create_view( view_name = 'ZCPWR_extract_resultsbis.csv' ).

  SELECT * FROM tadir INTO TABLE files.
  LOOP AT files INTO line.
*    IF line-author EQ 'BCUSER'.
    IF line-devclass EQ lt_tab.

      line_objects-objtype    = line-object.
      line_objects-objname    = line-obj_name.
      line_objects-responsibl = line-author.
      line_objects-devclass   = 'DEVEL'.
      line_objects-prgname    = ''.
      INSERT line_objects INTO TABLE p_objects.
    ENDIF.
  ENDLOOP.

  CALL METHOD search_function.
  CLEAR line_objects.
  CALL METHOD search_method.
  CLEAR line_objects.
  CALL METHOD search_prog.
  IF t_data IS INITIAL.

  ENDIF.
  DATA previous TYPE char40.
  previous = ''.
  LOOP AT t_data INTO s_data.
    IF s_data-object_type = 'FUGR' OR s_data-object_type = 'PROG'.
      IF s_data-incl_name <> previous.
        previous = s_data-incl_name.
        obj_name = s_data-object_name.
        obj_type = s_data-object_type.
        REFRESH l_i_prog_source .
        READ REPORT s_data-incl_name INTO l_i_prog_source .

        CHECK sy-subrc = 0.
        LOOP AT l_i_prog_source INTO l_was.
          CALL METHOD search_include
            EXPORTING
              p_line = l_was.
        ENDLOOP.
      ENDIF.
    ENDIF.
  ENDLOOP.
write( '<html><body>' ).
  myview->set_attribute( name = 'name' value = sy-uname ).
  CALL METHOD zcpwr_analyse_sci_data.
  myview->set_attribute( name = 'analysisbis' value = analysis ).
  call_view( myview ).
write( '</body></html>' ).
ENDMETHOD.