method SEARCH_METHOD .
DATA : clsn TYPE seoclsname.
TYPES : BEGIN OF seop_method_w_include,
          cpdkey TYPE seocpdkey,
          incname TYPE programm,
        END OF seop_method_w_include.
DATA methodline TYPE seop_method_w_include.
DATA  seop_methods_w_include TYPE TABLE OF seop_method_w_include .

  LOOP AT p_objects INTO line_objects.
    IF line_objects-objtype EQ 'CLAS'.
      clsn = line_objects-objname.
      cl_oo_classname_service=>get_all_method_includes(
      EXPORTING clsname = clsn
      RECEIVING result = seop_methods_w_include
      EXCEPTIONS
      class_not_existing = 2 ).
      LOOP AT seop_methods_w_include INTO methodline.
        s_data-object_type = line_objects-objtype.
        s_data-object_name = clsn.
        s_data-subobject_type = 'METH'.
        s_data-subobject_name = methodline-cpdkey-cpdname.
        s_data-incl_name = methodline-incname.
        APPEND s_data TO t_data.
      ENDLOOP.
    ENDIF.
  ENDLOOP.

endmethod.