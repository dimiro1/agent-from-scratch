(ns termagent.core
  (:require [termagent.openai :as openai]))

(defn read-user-input []
  (print "User: ")
  (flush)
  (read-line))

(defn -main []
  (loop [history []]
    (let [input (read-user-input)]
      (case input
        "exit" (println "Bye")
        nil? (println "Bye")
        (do
          (let [baseurl "https://api.openai.com/v1"
                api-key (System/getenv "OPENAI_API_KEY")
                model "gpt-4o-mini"
                history (conj history {:role "user" :content input})
                response (openai/generate baseurl api-key model history)
                ai-reply (openai/get-reply response)]
            (println "Assistant:" ai-reply)
            (recur (conj history {:role "assistant" :content ai-reply}))))))))
