<template>
  <v-app>
    <router-view/>
    <v-snackbar
      :timeout="snackbar.timeout"
      :top="snackbar.y === 'top'"
      :bottom="snackbar.y === 'bottom'"
      :right="snackbar.x === 'right'"
      :left="snackbar.x === 'left'"
      :multi-line="snackbar.mode === 'multi-line'"
      :vertical="snackbar.mode === 'vertical'"
      v-model="snackbar.show"
      :color="snackbar.color"
    >
      {{ snackbar.text }}
      <v-btn flat  @click.native="snackbar.show = false">关闭</v-btn>
    </v-snackbar>
  </v-app>
</template>

<script>
import { mapState } from "vuex";

export default {
  data() {
    return {
    };
  },

  computed: {
    ...mapState({
      snackbar:state=>state.snackbar,
    }),
  },
  methods: {
  },
  mounted(){
    
  },
  name: "App"
};
</script>
