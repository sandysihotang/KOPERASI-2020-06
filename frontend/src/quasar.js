import Vue from 'vue';

import './styles/quasar.styl';
import 'quasar/dist/quasar.ie.polyfills';
import '@quasar/extras/roboto-font/roboto-font.css';
import '@quasar/extras/material-icons/material-icons.css';
import '@quasar/extras/fontawesome-v5/fontawesome-v5.css';
import '@quasar/extras/ionicons-v4/ionicons-v4.css';
import '@quasar/extras/mdi-v3/mdi-v3.css';
import '@quasar/extras/eva-icons/eva-icons.css';
import {
  Quasar,
  QLayout,
  QHeader,
  QDrawer,
  QPageContainer,
  QPage,
  QToolbar,
  QToolbarTitle,
  QBtn,
  QIcon,
  QList,
  QItem,
  QItemSection,
  QItemLabel,
  QSpinnerAudio,
  QChatMessage,
  QSpinnerDots,
  QInput,
  QAvatar,
  QSpace,
  QMenu,
  QScrollArea,
  QFooter,
  Ripple,
} from 'quasar';

Vue.use(Quasar, {
  config: {},
  components: {
    QLayout,
    QHeader,
    QDrawer,
    QPageContainer,
    QPage,
    QToolbar,
    QToolbarTitle,
    QBtn,
    QIcon,
    QList,
    QItem,
    QItemSection,
    QItemLabel,
    QSpinnerAudio,
    QChatMessage,
    QSpinnerDots,
    QInput,
    QAvatar,
    QSpace,
    QMenu,
    QScrollArea,
    QFooter,
  },
  directives: {
    Ripple,
  },
  plugins: {},
});
