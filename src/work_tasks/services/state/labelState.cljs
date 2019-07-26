(ns work-tasks.services.state.labelState)

(defn update-labels [app-state payload]
  "updates the label list with the new values"
  (swap! app-state conj {:labels payload}))