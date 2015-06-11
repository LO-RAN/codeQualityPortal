METHOD SEARCH_INCLUDE .

  TYPES:
          BEGIN OF t_program,
            prog TYPE trdir-name,
            subc TYPE trdir-subc,
            text TYPE trdirt-text,
            sel(1),
           END OF t_program.

  DATA:
         l_pozicia TYPE i,
         l_wa_incl_search(72),
         l_include(72),
         l_next_search TYPE t_rep_source.

  DATA: g_i_progs TYPE STANDARD TABLE OF t_program.
  DATA t_prog_line TYPE t_program.
  IF p_line-line CP '*INCLUDE*'.
    CLEAR: l_include, l_next_search.
    MOVE sy-fdpos TO l_pozicia.
* Include found. Check if it is not a comment or something like that.
    CHECK NOT p_line-line+0(1) = '*' AND
          NOT p_line-line+0(1) = '"' .
    SEARCH p_line-line FOR '"' STARTING AT 1 ENDING AT l_pozicia.
    CHECK sy-subrc <> 0.
    ADD 7 TO l_pozicia.
    MOVE p_line-line+l_pozicia TO l_wa_incl_search.
    SHIFT l_wa_incl_search LEFT DELETING LEADING space.
    CHECK NOT l_wa_incl_search+0(1) = '"'.
    CHECK l_wa_incl_search NP 'STRUCTURE*' AND
          l_wa_incl_search NP 'TYPE*'.
    IF l_wa_incl_search CA '.'.
      l_pozicia = sy-fdpos.
      CHECK l_pozicia > 0.
      MOVE l_wa_incl_search+0(l_pozicia) TO l_include.
      CONDENSE l_include NO-GAPS.
      MOVE l_wa_incl_search+l_pozicia TO l_next_search-line.
    ELSE.
      CONDENSE l_wa_incl_search NO-GAPS.
      MOVE l_wa_incl_search TO l_include.
    ENDIF.
    CHECK NOT l_include IS INITIAL.
    TRANSLATE l_include TO UPPER CASE.
    READ TABLE g_i_progs INTO t_prog_line WITH KEY prog = l_include sel = 'X' .
    IF sy-subrc <> 0.
      CLEAR g_i_progs.
      SELECT SINGLE trdir~name trdir~subc trdirt~text
           INTO t_prog_line
            FROM trdir LEFT JOIN trdirt
              ON trdir~name = trdirt~name
            AND trdirt~sprsl = sy-langu
            WHERE trdir~name = l_include .

      data sline like line of t_include.
      IF sy-subrc = 0.
      sline-object_type = obj_type.
      sline-object_name = obj_name.
      sline-subobject_type = 'INCL'.
      sline-subobject_name = l_include.
      sline-incl_name = ''.
      APPEND sline TO t_include.
      ENDIF.
    ENDIF.
    IF NOT l_next_search IS INITIAL.
      CALL METHOD me->search_include
        EXPORTING
          p_line = l_next_search.
    ENDIF.
  ENDIF.

ENDMETHOD.