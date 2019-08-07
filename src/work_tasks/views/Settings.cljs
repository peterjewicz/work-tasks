(ns work-tasks.views.settings
  (:require [work-tasks.services.state.dispatcher :refer [handle-state-change]]))

(defn render [active]
  [:div.Page.Settings {:class active}
    [:p {:on-click #(handle-state-change {:type "update-active-view" :value "home"})}"Go Back"]
    [:h2 "I'm a page that takes my state"]])