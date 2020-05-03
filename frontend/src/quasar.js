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
  Loading,
  QTable,
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
  QCard,
  QCardSection,
  QForm,
  QCardActions,
  QSpinnerHourglass,
  QTabs,
  QRouteTab,
  QParallax,
  QToggle,
  QSeparator,
  QStepper,
  QStep,
  QTr,
  QTd,
  QStepperNavigation,
  QTooltip,
  QSelect,
  QPopupProxy,
  QDate,
  QUploader,
  QTab,
  QTabPanel,
  QTabPanels,
  QBadge,
  QExpansionItem,
  QField,
  QChip,
  QIntersection,
  QCheckbox,
  QDialog,
  ClosePopup,
  Notify,
  QBtnDropdown
} from 'quasar';

Vue.use(Quasar, {
  config: {},
  components: {
    QBtnDropdown,
    QDialog,
    QCheckbox,
    QField,
    QIntersection,
    QChip,
    QExpansionItem,
    QBadge,
    QTab,
    QTabPanel,
    QTabPanels,
    QUploader,
    QDate,
    QPopupProxy,
    QSelect,
    QTooltip,
    QTable,
    QLayout,
    QHeader,
    QDrawer,
    QPageContainer,
    QPage,
    QToolbar,
    QToolbarTitle,
    QBtn,
    QTr,
    QTd,
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
    QCard,
    QCardSection,
    QForm,
    QCardActions,
    QSpinnerHourglass,
    QTabs,
    QRouteTab,
    QParallax,
    QToggle,
    QSeparator,
    QStepper,
    QStep,
    QStepperNavigation,
  },
  directives: {
    Ripple,
    ClosePopup
  },
  plugins: {
    Loading,
    Notify
  },
});
