import React from "react";
import { lazyLoad } from "@/utils/loadable";

import LoadingScreen from "./components/LoadingScreen";

export default lazyLoad(() => import('.'), undefined, {
    fallback: React.createElement(LoadingScreen),
});

