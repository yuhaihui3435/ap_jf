#sql("byNameLike")
  SELECT * from s_gen_source where
  1=1
  #if(isNotBlank(name))
  NAME LIKE %#(name)%
  #end
#end