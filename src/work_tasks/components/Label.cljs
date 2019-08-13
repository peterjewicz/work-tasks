(ns work-tasks.components.Label)

(defn handle-update-label [id onChange]
  "no exta atom here, just pull it from the ID is fine
  onChange is a parital function that takes the new object"
  (onChange {:id id :name (.-value (.getElementById js/document (str "textInput" id)))}))

(defn Label [details onChange]
  [:div.Label {:key (:id details)}
    [:div.Label-left
      [:input {:type "test" :defaultValue (:name details) :id (str "textInput" (:id details)) }]
      [:div.Label-color {:style {:background (:color details)}}]]
    [:div.Label-right
      [:button.primary {:on-click #(handle-update-label (:id details) onChange)} "Update"]]])