(ns work-tasks.views.calendar
  (:require [work-tasks.services.state.dispatcher :refer [handle-state-change]]))

(defn render [active]
  [:div.Calendar {:class active}
    [:p {:on-click #(handle-state-change {:type "update-active-view" :value ""})}"Go Back"]
    [:h2 "I'm a page that takes my state"]])