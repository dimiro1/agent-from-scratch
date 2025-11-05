(ns termagent.openai
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]))

(defn generate [base-endpoint api-key model message]
  (let [url (str base-endpoint "/responses")
        payload {:model model
                 :input message}
        request {:url url
                 :method :post
                 :headers {"Authorization" (str "Bearer " api-key)
                           "Content-Type" "application/json"}
                 :accept "application/json"
                 :body (json/write-str payload)}
        response (http/request request)
        body (json/read-str (:body response) :key-fn keyword)]
    body))

(defn get-reply [response]
  (->> (get response :output)
       (filter #(= (get % :role) "assistant"))
       first
       :content
       first
       :text))
