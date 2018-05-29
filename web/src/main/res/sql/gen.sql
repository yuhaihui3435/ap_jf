#sql("byNameLike")
  SELECT * from s_gen_source where NAME LIKE %#(name)%
#end