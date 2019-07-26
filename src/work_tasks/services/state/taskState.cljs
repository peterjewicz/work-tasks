(ns work-tasks.services.state.taskState)

(defn update-active-task [app-state payload]
  "Set the current active task"
  (swap! app-state conj {:active-task payload}))

(defn update-tasks-list [app-state payload]
  "example of updating the state based on the payload"
  (swap! app-state conj {:tasks payload}))