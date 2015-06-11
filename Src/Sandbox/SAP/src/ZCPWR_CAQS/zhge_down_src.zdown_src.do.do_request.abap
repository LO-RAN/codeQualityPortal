METHOD do_request .
*http://<host>:<port>/sap/bc/bsp/sap/zcpwr_down_src/zdown_src.do?pkg=<package>%25&obj=<object>%25
*http://<host>:<port>/sap/bc/bsp/sap/zcpwr_down_src/zdown_src.do?pkg=<package>%25&obj=Z%25

* internal tables
  DATA :
  it_alltexts    TYPE STANDARD TABLE OF char512,
  it_include     TYPE STANDARD TABLE OF rpy_repo,
  it_source      TYPE STANDARD TABLE OF abapsource,
  it_source_ext  TYPE STANDARD TABLE OF abaptxt255,
  it_source_all  TYPE STANDARD TABLE OF abaptxt255,
  wa_source_ext  TYPE abaptxt255,
  it_texts       TYPE STANDARD TABLE OF textpool.
  DATA :
  p_objects       TYPE scit_objs,
   line_objects    TYPE scir_objs,
  p_results       TYPE scit_rest.

  DATA :
    g_proginfo  TYPE rpy_prog,
    p_progname  TYPE programm,
    myview      TYPE REF TO if_bsp_page,
    p_pkg       TYPE devclass,
    p_obj       TYPE objname.


  DATA : t_zsource TYPE STANDARD TABLE OF zcdsrc,
         wa_zsource TYPE zcdsrc.


  DATA : t_tadir TYPE SORTED TABLE OF tadir
         WITH NON-UNIQUE KEY  obj_name.
  DATA : wa_tadir    TYPE tadir.

* get url parameters
  lt_pkg = request->get_form_field( 'pkg' ).
  p_pkg = lt_pkg.
  lt_obj = request->get_form_field( 'obj' ).
  p_obj = lt_obj.

* select objects to process
  SELECT * FROM tadir INTO TABLE t_tadir
  WHERE object IN ('PROG', 'CLAS', 'WAPA', 'SICF', 'FUGR')
    AND devclass LIKE p_pkg
    AND obj_name LIKE p_obj.
*    AND author <> 'SAP'.

  LOOP AT t_tadir INTO wa_tadir.
  
* program identification
    p_progname = wa_zsource-repid = wa_tadir-obj_name.

* prepare inspection
    REFRESH : p_objects.

    line_objects-objtype    = wa_tadir-object.
    line_objects-objname    = wa_tadir-obj_name.
    line_objects-responsibl = wa_tadir-author.
    line_objects-devclass   = wa_tadir-devclass.
    line_objects-prgname    = ''.
    INSERT line_objects INTO TABLE p_objects.

* part 1 - invoke code inspecor
    CALL FUNCTION 'SCI_INSPECT_LIST'
      EXPORTING
        p_user                  = sy-uname
        p_objects               = p_objects
        p_name                  = 'DEFAULT'
        p_show_results          = space
      IMPORTING
        p_results               = p_results
      EXCEPTIONS
        no_object               = 1
        objs_already_exists     = 2
        no_default_checkvariant = 3
        too_many_objects        = 4
        could_not_read_variant  = 5
        OTHERS                  = 6.

    IF sy-subrc <> 0.
      MESSAGE ID sy-msgid TYPE sy-msgty NUMBER sy-msgno
              WITH sy-msgv1 sy-msgv2 sy-msgv3 sy-msgv4.
    ELSE.
      APPEND LINES  OF p_results TO wa_zsource-scit_rest.
    ENDIF.


* part 2 - get source related code
        CLEAR: it_include[], it_source[], it_source_ext[], it_texts[].

    CASE wa_tadir-object.
      WHEN 'CLAS' OR 'WAPA' or 'SICF' or 'FUGR'.
      WHEN OTHERS.

        CALL FUNCTION 'RPY_PROGRAM_READ'
          EXPORTING
            program_name     = p_progname
          IMPORTING
            prog_inf         = g_proginfo
          TABLES
            include_tab      = it_include
            SOURCE           = it_source
            source_extended  = it_source_ext
            textelements     = it_texts
          EXCEPTIONS
            cancelled        = 1
            not_found        = 2
            permission_error = 3
            OTHERS           = 4.
        IF sy-subrc <> 0.
          MESSAGE ID sy-msgid TYPE sy-msgty NUMBER sy-msgno
             WITH sy-msgv1 sy-msgv2 sy-msgv3 sy-msgv4.
        ELSE.
          APPEND LINES  OF it_source_ext TO wa_zsource-src.
        ENDIF.
    ENDCASE.

          INSERT wa_zsource INTO TABLE t_zsource.
          clear : wa_zsource.

  ENDLOOP.

  myview = create_view( view_name = 'zview_src.xml' ).

  myview->set_attribute( name = 'it_source' value = t_zsource ).
  call_view( myview ).


  .
ENDMETHOD.