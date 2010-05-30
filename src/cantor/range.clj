;;   Copyright (c) Zachary Tellman. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file epl-v10.html at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.

(ns cantor.range
  (:use [cantor.utils])
  (:require [cantor.vector :as vec])
  (:import [cantor.vector Vec2 Vec3]))

;;;

(defprotocol Range
  (#^vec/Tuple ul [r] "Returns the minima of the range.")
  (#^vec/Tuple lr [r] "Returns the maxima of the range.")
  (clone [r a b]))

(defn overlap?
  "Returns true if the two ranges overlap."
  [a b]
  (and
   (vec/all? <= (ul a) (lr b))
   (vec/all? >= (lr a) (ul b))))

(defn intersection
  "Returns the intersection of the two ranges, or nil if they don't intersect."
  [a b]
  (when (overlap? a b)
    (clone a
           (vec/map* max (ul a) (ul b))
           (vec/map* min (lr a) (lr b)))))

(defn union
  "Returns the union of the two ranges."
  [a b]
  (clone a
         (vec/map* min (ul a) (ul b))
         (vec/map* max (lr a) (lr b))))

(defn inside?
  "Returns true if vector 'p' is inside range 'r'."
  [r p]
  (and (vec/all? <= (ul r) p)
       (vec/all? >= (lr r) p)))

;;;

(defrecord Interval [#^double a #^double b]
  Range
  (ul [r] a)
  (lr [r] b)
  (clone [_ a b] (Interval. a b)))

(defrecord Box2 [#^Vec2 a #^Vec2 b]
  Range
  (ul [r] a)
  (lr [r] b)
  (clone [_ a b] (Box2. a b)))

(defrecord Box3 [#^Vec3 a #^Vec3 b]
  Range
  (ul [r] a)
  (lr [r] b)
  (clone [_ a b] (Box3. a b)))

;;;

(defn range?
  "Returns true if 'r' is a range."
  [r])

(defn interval
  "Creates a 1-D range."
  [a b]
  (Interval. a b))

(defn box2
  "Creates a 2-D range, or box."
  [ul lr]
  (Box2. ul lr))

(defn box3
  "Creates a 3-D range, or box."
  [ul lr]
  (Box3. ul lr))
