(ns work-tasks.views.task
  (:require [reagent.core :as reagent :refer [atom]]
            [work-tasks.scripts.api :as api]
            [work-tasks.services.state.dispatcher :refer [handle-state-change]]))

; (defn save-task [taskDetails]
;   (print @taskDetails))

(defn render [active]
  (let [taskDetails (atom {:title "" :details ""})]
    (fn [active]
      [:div.Task.Page {:class active}
        [:div.Task.header
          [:p {:on-click #(handle-state-change {:type "update-active-view" :value "home"})} "Go Back"]]
      [:div.Task.body
        [:input {:type "text" :on-change #(swap! taskDetails conj {:title (-> % .-target .-value)})}]
        [:textarea {:placeholder "Task Details" :on-change #(swap! taskDetails conj {:details (-> % .-target .-value)})}]
        [:h2 "TODO Date Stuff"]
        [:button {:on-click #(api/save-new-task taskDetails)} "Save"]]])))