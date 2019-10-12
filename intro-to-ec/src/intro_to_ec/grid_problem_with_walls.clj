(ns intro-to-ec.grid-problem-with-walls)

(defn origin-goal?
  "A goal checking function that assumes the target
   position is the origin, i.e., [0 0]."
  [[x y]]
  (and (zero? x) (zero? y)))

; Finds distance of a coordiante to goal by summing the values of the
; x and y coordiante
(defn heuristic
[a & args]
(+ (Math/abs (first a)) (Math/abs (second a))))

;This time we emulate real world scenario by randomly adding 0 or 1 to
; to each coordiante value since real world data has "noise"
(defn heuristic2
[b & args]
(+ (+ (Math/abs (first b)) (rand-int 2)) (+ (Math/abs (second b)) (rand-int 2)))

;Now we are purposefully chosing to account in the x-coordinate sometimes and
; ignore it's contributions other times
(defn heuristic3
[c & args]
(+ (* (Math/abs (first c)) (rand-int 2)) (Math/abs (second c))))

;; The possible moves in this lattice world. Each
;; move is represented by a vector indicating the
;; change in both x and y coordinates associated
;; with this move.
(def up    [0  1])
(def down  [0 -1])
(def left  [-1  0])
(def right [1  0])
(def all-moves [up down left right])

(defn apply-move
  "Apply a move to a given position, yielding the new position"
  [position move]
  (vec (map + position move)))

(defn legal-coordinate
  "Limit our search to the space where the given coordinate
   is in the range [0, max-range)."
  [min-range max-range x]
  (and (>= x min-range) (< x max-range)))

(defn legal-state
  "Return true if both coordinates are legal and this position isn't
   in the 'wall' set."
  [min-range max-range wall-set position]
  (and (every? (partial legal-coordinate min-range max-range) position)
       (not (contains? wall-set position))))

(defn grid-children
  "Generate a list of all the possible child states
   reachable from the given initial position."
  [min-range max-range wall-set position]
  (filter (partial legal-state min-range max-range wall-set)
          (map (partial apply-move position) all-moves)))

(def min-range -10)
(def max-range 10)
(def no-walls #{})
(def low-vertical-wall
  (set
   (for [y (range -2 8)]
     [1 y])))
(def u-shape
  (set
   (concat
    (for [x (range -5 3)]
      [x 1])
    (for [x (range -5 3)]
      [x -1])
    [[2 0]])))

(defn make-grid-problem
  "Create an instance of a simple problem of moving on a grid towards
   the origin. The ranges specify the bounds on the grid world, and the
   `wall-set` is a (possibly empty) set of positions that can't be entered
   or crossed."
  [min-range max-range wall-set]
  {:goal? origin-goal?
   :make-children (partial grid-children min-range max-range wall-set)
   :heuristic heuristic})
