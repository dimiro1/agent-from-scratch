(ns termagent.core
  (:require [clojure.string :as str]
            [termagent.config :as config]
            [termagent.openai :as openai]
            [termagent.ui :as ui]))

(defn with-shutdown-hook
  "Register a shutdown hook that runs `f` unless cancelled.
   Returns a function that, when called, prevents the hook from running."
  [f]
  (let [cancelled (atom false)]
    (.addShutdownHook (Runtime/getRuntime)
                      (Thread. (fn []
                                 (when-not @cancelled
                                   (f)))))
    #(reset! cancelled true)))

(defn exit-command?
  "Return true when the input is \"exit\" or nil (EOF)."
  [input]
  (or (nil? input)
      (= "exit" input)))

(defn call-assistant
  "Generate an assistant reply using the supplied chat history and configuration."
  [base-url api-key model history]
  (-> (openai/generate base-url api-key model history)
      (openai/get-reply)))

(defn update-history
  "Append a message with `role` and `content` to the chat history."
  [history role content]
  (conj history {:role role :content content}))

(defn -main []
  (let [base-url (config/base-url)
        api-key (config/api-key)
        model (config/model)
        cancel-shutdown (with-shutdown-hook ui/print-goodbye)]
    (ui/print-banner model)
    (loop [history []]
      (let [input (ui/read-input)]
        (cond
          (exit-command? input) (do
                                  (cancel-shutdown)
                                  (ui/print-goodbye))
          (str/blank? input) (recur history)
          :else
          (let [history  (update-history history "user" input)
                response (ui/with-loading #(call-assistant base-url api-key model history))
                history  (update-history history "assistant" response)]
            (ui/print-response response)
            (recur history)))))))
