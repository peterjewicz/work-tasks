(ns work-tasks.services.state.taskState)

(defn update-tasks-list [app-state payload]
  "example of updating the state based on the payload"
  (swap! app-state conj {:tasks payload}))