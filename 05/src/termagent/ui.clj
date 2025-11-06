(ns termagent.ui
  (:require [clojure.string :as str]
            [termagent.ansi :as ansi]
            [termagent.markdown :as md]))

(def ^:private left-padding "  ")

(def ^:private loading-messages
  ["consulting the oracle"
   "reading tea leaves"
   "channeling cosmic wisdom"
   "asking the magic 8-ball"
   "staring into the void"
   "herding cats"
   "tickling the neurons"
   "brewing digital coffee"
   "decoding the matrix"
   "communing with electrons"
   "juggling bits"
   "wrangling photons"
   "divining the answer"
   "shaking the snow globe"
   "counting backwards from infinity"
   "untangling spaghetti code"
   "persuading the bits"
   "negotiating with packets"])

(defn- random-loading-message
  "Return a random loading message."
  []
  (rand-nth loading-messages))

(defn- print-padded
  "Print text with left padding."
  [& args]
  (print left-padding)
  (apply print args))

(defn- println-padded
  "Print text with left padding and newline."
  [& args]
  (print left-padding)
  (apply println args))

(defn print-banner
  "Print a welcome banner when the application starts."
  [model]
  (let [directory (System/getProperty "user.dir")]
    (println)
    (println-padded (str (ansi/render [:bold :blue "│ "]) "→ _ Terminal Agent (v0.1)"))
    (println-padded (ansi/render [:bold :blue "│ "]))
    (println-padded (str (ansi/render [:bold :blue "│ "]) "model:     " (ansi/render [:cyan model])))
    (println-padded (str (ansi/render [:bold :blue "│ "]) "directory: " (ansi/render [:dim directory])))
    (println)))

(defn print-separator
  "Print a subtle visual separator between exchanges."
  []
  (println-padded (ansi/render [:dim "---"]))
  (println))

(defn read-input
  "Prompt the user for input with left padding and return the entered line."
  []
  (print-padded (ansi/render [:cyan "→ "]))
  (flush)
  (read-line))

(defn print-response
  "Print the assistant's response with left padding and markdown rendering."
  [response]
  (let [lines (str/split-lines (md/render response))]
    (when (first lines)
      (print-padded (ansi/render [:dim "• "]))
      (println (first lines)))
    (doseq [line (rest lines)]
      (println-padded line)))
  (print-separator))

(defn print-goodbye
  "Print a goodbye message with left padding."
  []
  (println)
  (println-padded "Bye"))

(defn with-loading
  "Execute a function while showing a loading animation."
  [f]
  (println)
  (let [running (atom true)
        spinner-chars ["⠋" "⠙" "⠹" "⠸" "⠼" "⠴" "⠦" "⠧" "⠇" "⠏"]
        message (random-loading-message)
        spinner-task (future
                       (loop [idx 0]
                         (when @running
                           (print (str "\r" left-padding (ansi/render [:blue (get spinner-chars idx)]) " " (ansi/render [:dim (str message "…")])))
                           (flush)
                           (Thread/sleep 100)
                           (recur (mod (inc idx) (count spinner-chars))))))
        result (f)]
    (reset! running false)
    @spinner-task
    (print (str "\r" left-padding "                        \r"))
    (flush)
    result))
