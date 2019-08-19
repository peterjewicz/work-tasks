(ns work-tasks.views.settings
  (:require [work-tasks.services.state.dispatcher :refer [handle-state-change]]
            [work-tasks.components.Label :refer [Label]]
            [work-tasks.scripts.api :as api]
            ["moment" :as moment]))

(defn format-labels [label newLabelProps]
  (if (= (:id label) (:id newLabelProps))
    (conj label newLabelProps)
    label))

(defn handle-label-update [labels newLabelProps]
  "maps through our labels and merges in the new properties"
  (api/update-labels (map (fn [label] (format-labels label newLabelProps)) labels)))

(defn render [active labels completedTasks]
  [:div.Page.Settings {:class active}
    [:div.Settings.Settings-Header
      [:i.fas.fa-arrow-left {:on-click #(handle-state-change {:type "update-active-view" :value "home"})}]]
    [:h2 "My Labels"]
    (for [label labels]
      (Label label (partial handle-label-update labels)))
    [:h2 "Completed Tasks"]
    (for [task completedTasks]
      [:div.CompletedTask {:key (:id task)}
        [:p (str (:title task) " - Completed On: " (.format (moment (:completedOn task)) "MM/DD/YYYY"))]])])