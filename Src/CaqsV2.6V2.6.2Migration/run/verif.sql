Select pere.desc_elt, pere.id_telt, fils.desc_elt
From Element pere, Element fils, Elt_links l
Where l.elt_pere=pere.id_elt
And l.elt_fils=fils.id_elt
And (fils.id_telt='CLS' or fils.id_telt='PKG')
And fils.id_pro='20060721152401781962450'
And pere.id_pro=fils.id_pro
order by pere.desc_elt