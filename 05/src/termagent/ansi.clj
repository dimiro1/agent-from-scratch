(ns termagent.ansi)

(def codes
  {:reset "\033[0m"
   :bold "\033[1m"
   :dim "\033[2m"
   :italic "\033[3m"
   :underline "\033[4m"
   :red "\033[31m"
   :green "\033[32m"
   :yellow "\033[33m"
   :blue "\033[34m"
   :magenta "\033[35m"
   :cyan "\033[36m"
   :white "\033[37m"})

(defn render
  "Renders text with ANSI codes. Takes a vector [style text] or [style1 style2 ... text]
   where styles are keywords and text is a string."
  [v]
  (if (vector? v)
    (let [styles (butlast v)
          text (last v)
          style-codes (apply str (map codes styles))]
      (str style-codes text (:reset codes)))
    v))
