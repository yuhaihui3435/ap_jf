package com.sc.ap.query;
import cn.hutool.core.util.StrUtil;
import com.sc.ap.kits.DateKit;
import com.sc.ap.core.CoreQuery;
/**
 * Generated by ap-jf.
 */
@SuppressWarnings("serial")
public class ResQuery extends CoreQuery {

        private String name;
        public void setName(String name){
                this.name=name;
        }
        public String getName(){
            return this.name;
        }
        private String enabled;
        public void setEnabled(String enabled){
                this.enabled=enabled;
        }
        public String getEnabled(){
            return this.enabled;
        }
        private String code;
        public void setCode(String code){
                this.code=code;
        }
        public String getCode(){
            return this.code;
        }


}