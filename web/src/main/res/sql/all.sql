#namespace("genSql")
  #include("gen.sql")
#end

#sql("queryByAndCond")
  select * from #(table) where 1=1
    #for(x : cond)
        #if(x.key=='orderby')
          #set(orderby=para(x.value))
        #elseif(x.key=='dAt')
          #set(dAt='and dAt is null')
        #else
          and #(x.key)   #para(x.value)
        #end
    #end
    #(dAt) #(orderby)
#end
