(ns work-tasks.views.settings
  (:require [work-tasks.services.state.dispatcher :refer [handle-state-change]]
            [work-tasks.components.Label :refer [Label]]
            [work-tasks.scripts.defaultLabels :refer [labels]]))

(defn render [active]
  [:div.Page.Settings {:class active}
    [:div.Settings.Settings-Header
      [:p {:on-click #(handle-state-change {:type "update-active-view" :value "home"})}"Go Back"]]
    [:h2 "My Labels"]
    (for [label labels]
      (Label label))])