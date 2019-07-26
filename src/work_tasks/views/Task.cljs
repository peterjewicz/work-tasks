(ns work-tasks.views.task
  (:require [reagent.core :as reagent :refer [atom]]
            [work-tasks.scripts.api :as api]
            [work-tasks.services.state.dispatcher :refer [handle-state-change]]))

;TODO to make the edit functionality work
; Remove the active task from the state once we hit here or we will never be able to edit...
; Add a hidden input with the current ID, we can create a multi method to dispatch on it's existance
; This way we can easily differentiate edit vs ad transparently to this component
; Finally, we should 'touch' the states as we leave so that going from edit -> doesn't carry over the values


(defn render [active activeTask]
  (let [taskDetails (atom {:title "" :details ""})
        taskKeys (atom [1, 2])] ; we use this to force re-render on the input
    (fn [active activeTask]
      (if activeTask
        (do
          (swap! taskDetails conj {:title (:title activeTask) :details (:details activeTask)}); TODO there's probably a merge function that just does this
          (reset! taskKeys [(inc (first @taskKeys)) (inc (second @taskKeys))]) ; TODO make this better
        ))
      (print @taskDetails)
      [:div.Task.Page {:class active}
        [:div.Task.header
          [:p {:on-click #(handle-state-change {:type "update-active-view" :value "home"})} "Go Back"]]
      [:div.Task.body
        [:input {:type "text" :key (first @taskKeys) :defaultValue (:title @taskDetails) :name "title" :on-change #(swap! taskDetails conj {:title (-> % .-target .-value)})}]
        [:textarea {:placeholder "Task Details" :key (second @taskKeys) :defaultValue (:details @taskDetails) :name "details" :on-change #(swap! taskDetails conj {:details (-> % .-target .-value)})}]
        [:h2 "TODO Date Stuff"]
        [:button {:on-click #(api/save-new-task taskDetails)} "Save"]]])))