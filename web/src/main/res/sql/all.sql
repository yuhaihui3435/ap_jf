#namespace("userSql")
  ###include("user.sql")
#end

#sql("queryByAndCond")
  select * from #(table) where 1=1
    #for(x : cond)
        #if(x=='orderby')
          #set(orderby=para(x.value));
        #else
          and #(x.key) #para(x.value)
        #end
    #end
    #(orderby)
#end
