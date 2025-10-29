<template>
    <i class="download icon"></i>
    <div id="controls">
        <input type="file" id="bgImage" accept="image/*" v-on:change="(e) => updateFile(e)">
    </div>
  <v-stage
    ref="stage"
    :config="stageSize"
    @dragstart="handleDragstart"
    @dragend="handleDragend"
  >
    <v-layer ref="layer">
      <v-star
        v-for="item in list"
        :key="item.id"
        :config="{
          x: item.x,
          y: item.y,
          rotation: item.rotation,
          id: item.id,
          numPoints: 5,
          innerRadius: 30,
          outerRadius: 50,
          fill: '#89b717',
          opacity: 0.8,
          draggable: true,
          scaleX: dragItemId === item.id ? item.scale * 1.2 : item.scale,
          scaleY: dragItemId === item.id ? item.scale * 1.2 : item.scale,
          shadowColor: 'black',
          shadowBlur: 10,
          shadowOffsetX: dragItemId === item.id ? 15 : 5,
          shadowOffsetY: dragItemId === item.id ? 15 : 5,
          shadowOpacity: 0.6
        }"
      />
    </v-layer>
    <v-layer>
      <v-image
        v-if="yodaImage"
        :config="{
          x: 50,
          y: 50,
          image: yodaImage,
          width: 106,
          height: 118
        }"
      />
      <v-image
        v-if="vaderImage"
        :config="{
          x: 200,
          y: 50,
          image: vaderImage,
          scaleX: 0.5,
          scaleY: 0.5,
          cornerRadius: 20
        }"
      />
      <v-image
        v-if="myImage"
        :config="{
          x: 800,
          y: 50,
          image: myImage,
          scaleX: 0.5,
          scaleY: 0.5,
          width: 400,
          height: 400,
          cornerRadius: 400
        }"
      />
    </v-layer>
  </v-stage>
</template>


<script setup>
import { ref, onMounted } from 'vue';
import { useImage } from 'vue-konva';

const stageSize = {
  width: window.innerWidth,
  height: window.innerHeight
};

const list = ref([]);
const dragItemId = ref(null);
const [yodaImage] = useImage('https://konvajs.org/assets/yoda.jpg');
const [vaderImage] = useImage('https://konvajs.org/assets/darth-vader.jpg');
let [myImage] = useImage('https://konvajs.org/assets/darth-vader.jpg');

const handleDragstart = (e) => {
  // save drag element:
  dragItemId.value = e.target.id();
  // move current element to the top:
  const item = list.value.find(i => i.id === dragItemId.value);
  const index = list.value.indexOf(item);
  list.value.splice(index, 1);
  list.value.push(item);
};

const handleDragend = () => {
  dragItemId.value = null;
};

onMounted(() => {
  for (let n = 0; n < 30; n++) {
    list.value.push({
      id: Math.round(Math.random() * 10000).toString(),
      x: Math.random() * stageSize.width,
      y: Math.random() * stageSize.height,
      rotation: Math.random() * 180,
      scale: Math.random()
    });
  }
});


function updateFile(e){
    alert(e)
}
</script>