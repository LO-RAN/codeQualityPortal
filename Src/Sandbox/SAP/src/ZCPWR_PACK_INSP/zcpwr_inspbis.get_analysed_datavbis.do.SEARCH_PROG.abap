METHOD SEARCH_PROG .

  LOOP AT p_objects INTO line_objects.
    IF line_objects-objtype = 'PROG'.
      s_data-object_type = line_objects-objtype.
      s_data-object_name = line_objects-objname.
      s_data-subobject_type = ''.
      s_data-subobject_name = ''.
      s_data-incl_name = line_objects-objname.
      APPEND s_data TO t_data.

    ENDIF.
  ENDLOOP.

ENDMETHOD.