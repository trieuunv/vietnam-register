import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { Provider } from 'react-redux';
import { ConfigProvider } from 'antd';
import './index.css';
import App from './App.tsx';
import store from './store/store.ts';

import '@fontsource/montserrat';
import '@fontsource/montserrat/700.css';
import '@fontsource/montserrat/900.css';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <Provider store={store}>
      <ConfigProvider
        theme={{
          token: {
            fontFamily: 'Montserrat, sans-serif',  // Cài đặt font mặc định
            borderRadius: 4,  // Border radius mặc định cho tất cả component
          },
          components: {
            Button: {
              borderRadius: 4,   // Button bo góc 4px
            },
            Input: {
              borderRadius: 4,   // Input bo góc 4px
            },
            Select: {
              borderRadius: 4,   // Select bo góc 4px
            },
            DatePicker: {
              borderRadius: 4,   // DatePicker bo góc 4px
            },
            Tag: {
              borderRadius: 4,   // Tag bo góc 4px
            },
            Table: {
              borderRadius: 6,   // Table bo góc 6px cho mềm mại hơn
            },
            Card: {
              borderRadiusLG: 8,  // Card bo góc lớn 8px
            },
            Modal: {
              borderRadiusLG: 8,  // Modal bo góc lớn 8px
            },
            Drawer: {
              borderRadiusLG: 8,  // Drawer bo góc lớn 8px
            },
            Alert: {
              borderRadius: 6,   // Alert bo góc 6px
            },
            Message: {
              borderRadius: 6,   // Message bo góc 6px
            },
            Notification: {
              borderRadius: 6,   // Notification bo góc 6px
            },
            Avatar: {
              borderRadius: 50,  // Avatar bo góc 50% (hình tròn)
            },
            Steps: {
              borderRadius: 6,   // Steps bo góc 6px
            },
            Popover: {
              borderRadius: 6,   // Popover bo góc 6px
            },
            Dropdown: {
              borderRadius: 6,   // Dropdown bo góc 6px
            },
            Progress: {
              borderRadius: 6,   // Progress bo góc 6px
            },
            Switch: {
              borderRadius: 6,   // Switch bo góc 6px
            },
            Collapse: {
              borderRadius: 6,   // Collapse bo góc 6px
            },
            Rate: {
              borderRadius: 6,   // Rate bo góc 6px
            },
            Timeline: {
              borderRadius: 6,   // Timeline bo góc 6px
            },
            Slider: {
              borderRadius: 6,   // Slider bo góc 6px
            },
          }
        }}
      >
        <App />
      </ConfigProvider>
    </Provider>
  </StrictMode>,
);
