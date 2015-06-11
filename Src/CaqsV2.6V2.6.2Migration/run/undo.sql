delete from elt_links where elt_fils in (select id_elt from element where id_telt='PKG' and id_pro='20060721152401781962450');
delete from elt_links where elt_pere in (select id_elt from element where id_telt='PKG' and id_pro='20060721152401781962450');
delete from element where id_telt='PKG' and id_pro='20060721152401781962450';
insert into elt_links (elt_pere, elt_fils, type) select id_main_elt, id_elt, 'L' from element where id_telt='CLS' and id_pro='20060721152401781962450';
commit;
