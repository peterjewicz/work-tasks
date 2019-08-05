(ns work-tasks.core
    (:require [reagent.core :as reagent :refer [atom]]
              [work-tasks.views.home :as home]
              [work-tasks.views.task :as task]
              [work-tasks.views.settings :as settings]
              [work-tasks.services.state.global :refer [app-state]]
              [work-tasks.scripts.api :as api]
              [work-tasks.scripts.notifications :as notification]
              [work-tasks.services.state.dispatcher :refer [handle-state-change]]))

(enable-console-print!)

(api/update-tasks-in-store)

(defn core []
  ; (print (:notification @app-state))
  [:div.Main
    [notification/Notification (:notification @app-state)]
    [home/render (:home (:active-page @app-state)) (:tasks @app-state)]
    [task/render (:task (:active-page @app-state)) (:active-task @app-state)]
    [settings/render (:settings (:active-page @app-state))]])


(reagent/render-component [core]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)