<template>
  <div id="about" align="center">
    <h1>
      Video Call
    </h1>
    <h4>My feed: </h4>
    <div id="me" style="height: 100px; width: 100px;"></div>

    <h4>Remote feeds :</h4>
    <div id="remote-container"></div>

    <h4>Canvas Feeds :</h4>
    <div id="canvas-container"></div>
  </div>
</template>
<script>
import AgoraRTC from 'agora-rtc-sdk';

export default {
  data() {
    return {
      remoteContainer: null,
      canvasContaner: null,
      client: null,
    };
  },
  methods: {
    // eslint-disable-next-line no-unused-vars
    handleFail(err) {
    },
    addVideoStream(streamId) {
      const streamDiv = document.createElement('div');
      streamDiv.id = streamId;
      streamDiv.style.transform = 'rotateY(180deg)';
      streamDiv.style.width = '100px';
      streamDiv.style.height = '100px';
      this.remoteContainer.appendChild(streamDiv);
    },
    removeVideoStream(evt) {
      const { stream } = evt;
      stream.stop();
      const remDiv = document.getElementById(stream.getId());
      remDiv.parentNode.removeChild(remDiv);
    },
    addCanvas(streamID) {
      const video = document.getElementById(`video${streamID}`);
      const canvas = document.createElement('canvas');
      this.canvasContaner.appendChild(canvas);
      const ctx = canvas.getContext('2d');
      video.addEventListener('loadedmetadata', () => {
        canvas.width = video.videoWidth;
        canvas.height = video.videoHeight;
      });

      video.addEventListener('play', () => {
        const $this = this;
        (function loop() {
          if (!$this.paused && !$this.ended) {
            if ($this.widows !== canvas.width) {
              canvas.width = $this.videoWidth;
              canvas.height = $this.videoHeight;
            }
            ctx.drawImage($this, 0, 0);
            setTimeout(loop, 1000 / 30);
          }
        }());
      }, 0);
    },
  },
  mounted() {
    this.remoteContainer = document.getElementById('remote-container');
    this.canvasContaner = document.getElementById('canvas container');

    this.client = AgoraRTC.createClient({
      mode: 'live',
      codec: 'h264',
    });
    this.client.init('ad66aaf4f6914593abc5ef00405ad6c0', () => {

    });
    this.client.join(null, 'agora-demo', null, (uid) => {
      const localStream = AgoraRTC.createStream({
        streamID: uid,
        audio: false,
        video: true,
        screen: false,
      });

      localStream.init(() => {
        localStream.play('me');
        this.client.publish(localStream, this.handleFail);
        this.client.on('stream-added', (evt) => {
          this.client.subscribe(evt.stream, this.handleFail);
        });

        this.client.on('stream-subscribed', (evt) => {
          const { stream } = evt;
          this.addVideoStream(`${stream.getId()}`);
          stream.play(`${stream.getId()}`);
          this.addCanvas(`${stream.getId()}`);
        });

        this.client.on('stream-removed', this.removeVideoStream);
      }, this.handleFail);
    }, this.handleFail);
  },
};
</script>
<style>

</style>
