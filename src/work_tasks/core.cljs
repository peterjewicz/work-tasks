(ns work-tasks.core
    (:require [reagent.core :as reagent :refer [atom]]
              [work-tasks.views.home :as home]
              [work-tasks.views.task :as task]
              [work-tasks.views.settings :as settings]
              [work-tasks.views.calendar :as calendar]
              [work-tasks.services.state.state :refer [app-state]]
              [work-tasks.scripts.api :as api]
              [work-tasks.scripts.notifications :as notification]
              [work-tasks.scripts.taskHelpers :as taskHelpers]
              [work-tasks.services.state.dispatcher :refer [handle-state-change]]
              ["moment" :as moment]))

(enable-console-print!)

(api/update-tasks-in-store)
(api/setup-initial-labels)

(defn core []
  ; (print (:labels @app-state))
  [:div.Main
    [notification/Notification (:notification @app-state)]
    [home/render (:home (:active-page @app-state)) (:tasks @app-state)]
    [task/render (:task (:active-page @app-state)) (:active-task @app-state) (:labels @app-state)]
    [settings/render (:settings (:active-page @app-state)) (:labels @app-state) (taskHelpers/get-completed-tasks (:tasks @app-state))]
    [calendar/render (:calendar (:active-page @app-state)) (taskHelpers/filter-completed-tasks (:tasks @app-state))]])


(reagent/render-component [core]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
