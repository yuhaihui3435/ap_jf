<template>
    <v-dialog v-model="dialog" max-width="290" persistent>
      <v-card>
        <v-card-title class="headline">{{title}}</v-card-title>
        <v-card-text>{{msg}}</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn v-for="(item,index) in btns" :color="item.color" :key="index" flat="flat" @click.native="item.action">{{item.label}}</v-btn>
         
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>
<script>
export default {
    data(){
        return{
            dialog:false,
            title:'提示',
            msg:'确认要执行这个操作吗？',
            btns:[],
        }
    },
    methods:{
        
    },
    mounted(){
        
    }

}
</script>

