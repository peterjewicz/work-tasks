(ns work-tasks.services.state.notificationState)

(defn update-notification-state [app-state payload]
  "Set the current active task"
  (swap! app-state assoc-in [:notification :display] 5))