(ns termagent.markdown
  (:require [clojure.string :as str]
            [termagent.ansi :as ansi]))

(defn render-bold [text]
  (str/replace text
               #"\*\*(.+?)\*\*"
               (fn [[_ content]]
                 (ansi/render [:bold content]))))

(defn render-italic [text]
  (str/replace text
               #"\*(.+?)\*"
               (fn [[_ content]]
                 (ansi/render [:italic content]))))

(defn render-strikethrough [text]
  (str/replace text
               #"~~(.+?)~~"
               (fn [[_ content]]
                 (ansi/render [:dim content]))))

(defn render-inline-code [text]
  (str/replace text
               #"`(.+?)`"
               (fn [[_ content]]
                 (ansi/render [:cyan content]))))

(defn render-headers [text]
  (-> text
      (str/replace #"(?m)^###### (.+)$"
                   (fn [[_ content]]
                     (ansi/render [:bold :white content])))
      (str/replace #"(?m)^##### (.+)$"
                   (fn [[_ content]]
                     (ansi/render [:bold :white content])))
      (str/replace #"(?m)^#### (.+)$"
                   (fn [[_ content]]
                     (ansi/render [:bold :yellow content])))
      (str/replace #"(?m)^### (.+)$"
                   (fn [[_ content]]
                     (ansi/render [:bold :yellow content])))
      (str/replace #"(?m)^## (.+)$"
                   (fn [[_ content]]
                     (ansi/render [:bold :cyan content])))
      (str/replace #"(?m)^# (.+)$"
                   (fn [[_ content]]
                     (ansi/render [:bold :magenta content])))))

(defn render-images [text]
  (str/replace text
               #"!\[(.+?)\]\((.+?)\)"
               (fn [[_ alt-text url]]
                 (str "Image: " alt-text " → " (ansi/render [:dim url])))))

(defn render-links [text]
  (str/replace text
               #"\[(.+?)\]\((.+?)\)"
               (fn [[_ link-text url]]
                 (str (ansi/render [:blue :underline link-text]) " → " (ansi/render [:dim url])))))

(defn render-tasks [text]
  (-> text
      (str/replace #"(?m)^(\s*)[-*+] \[x\] (.+)$"
                   (fn [[_ indent content]]
                     (str indent (ansi/render [:green "[✓]"]) " " content)))
      (str/replace #"(?m)^(\s*)[-*+] \[X\] (.+)$"
                   (fn [[_ indent content]]
                     (str indent (ansi/render [:green "[✓]"]) " " content)))
      (str/replace #"(?m)^(\s*)[-*+] \[ \] (.+)$"
                   (fn [[_ indent content]]
                     (str indent "[ ] " content)))))

(defn render-lists [text]
  (-> text
      (str/replace #"(?m)^(\s*)[-*+] (.+)$"
                   (fn [[_ indent content]]
                     (str indent (ansi/render [:cyan "• "]) content)))
      (str/replace #"(?m)^(\s*)(\d+)\. (.+)$"
                   (fn [[_ indent number content]]
                     (str indent (ansi/render [:cyan (str number ". ")]) content)))))

(defn render-blockquotes [text]
  (str/replace text
               #"(?m)^> (.+)$"
               (fn [[_ content]]
                 (ansi/render [:dim (str "│ " content)]))))

(defn render-horizontal-rules [text]
  (str/replace text
               #"(?m)^(---|\*\*\*|___)$"
               (fn [[_]]
                 (ansi/render [:dim "────────────────────────────────"]))))

(defn render [text]
  (-> text
      render-horizontal-rules
      render-headers
      render-blockquotes
      render-tasks
      render-lists
      render-images
      render-links
      render-bold
      render-strikethrough
      render-italic
      render-inline-code))

(defn showcase
  "Demonstrates all markdown rendering features.

  Run with: clj -M -e \"(require '[termagent.markdown :as md]) (md/showcase)\""
  []
  (let [sample-text "# Markdown Elements Showcase

## Headers
# This is an H1
## This is an H2
### This is an H3
#### This is an H4
##### This is an H5
###### This is an H6

## Text Styling
- **Bold Text**
- *Italic Text*
- ~~Strikethrough~~

## Lists
### Unordered List
- Item 1
- Item 2
  - Subitem 2.1
  - Subitem 2.2

### Ordered List
1. First Item
2. Second Item
   1. Subitem 2.1
   2. Subitem 2.2

## Links
[OpenAI](https://openai.com)

## Images
![Clojure Logo](https://clojure.org/images/clojure-logo-120b.png)

## Tasks
- [x] Completed task
- [ ] Incomplete task
- [X] Another completed task

## Code
Here is some `inline code` example.

## Blockquotes
> This is a blockquote with important information.

## Horizontal Rule
---

End of showcase."]
    (println (render sample-text))))
