var RxHTML = (function () {
  var self = {};

  var templates = {};
  var router = {};
  var connection = new Adama.Connection(Adama.Production);
  var connections = {};

  connection.start();

  var rootReplace = "/";
  var fixPath = function (path) {
    return path;
  };
  if (window.location.hostname.endsWith(".adama-platform.com") && !window.location.hostname.endsWith("ide.adama-platform.com")) {
    var parts = window.location.pathname.split("/");
    rootReplace = [parts[0], parts[1], parts[2], ""].join("/");
    var offset = parts[0].length + parts[1].length + parts[2].length + 2;
    fixPath = function (path) {
      return path.substring(offset);
    };
  }

  var fixHref = function (href) {
    if (href.startsWith("/")) {
      return rootReplace + href.substring(1);
    }
    return href;
  };

  var get_connection_obj = function (name) {
    if (name in connections) {
      return connections[name];
    } else {
      var obj = {
        name: name,
        ptr: null,
        tree: new AdamaTreeSimple(),
        outstanding: {},
        decisions: {},
        id: 0
      };
      obj.subscribe = function (channel, callback) {
        var s = channel + "|" + this.id++;
        this.decisions[s] = callback;
        return function () {
          delete this.decisions[s];
        }.bind(this);
      }.bind(obj);
      obj.ondecide = function (outstanding) {
        for (var ch in obj.outstanding) {
          obj.outstanding[ch] = {options: []};
        }
        var n = outstanding.length;
        for (var k = 0; k < n; k++) {
          var o = outstanding[k];
          obj.outstanding[o.channel] = o;
        }
        for (var ch in obj.outstanding) {
          for (var sub in obj.decisions) {
            if (sub.startsWith(ch + "|")) {
              obj.decisions[sub]();
            }
          }
        }
      };
      connections[name] = obj;
      return obj;
    }
  };

  self.make = function () {
    return new AdamaTreeSimple();
  };

  // HELPER | subscribe the given 'sub' to changes within state for the given field named name
  var subscribe = function (state, name, sub) {
    var ss = self.pI(state, name);
    var s = ss[ss.current];
    if ("@e" in s.delta) {
      s.delta["@e"].push(sub);
    } else {
      s.delta["@e"] = [sub];
    }
  };

  // HELPER | create fresh state
  var fresh = function (where) {
    return {
      tree: new AdamaTreeSimple(),
      delta: {},
      parent: null,
      path: null,
      where: where
    };
  };

  // HELPER | create a new delta copy from the given specific state (i.e. either data or view)
  var new_delta_copy = function (ss) {
    if (ss == null) {
      return null;
    }
    var parent = null;
    if (ss.parent != null) {
      parent = new_delta_copy(ss.parent);
    }
    var new_delta = {};
    if (parent != null) {
      parent.delta[ss.path] = new_delta;
    }
    return {tree: ss.tree, parent: parent, delta: new_delta, path: ss.path};
  };

  // HELPER | construct a path to
  var path_to = function (ss, obj) {
    if (ss.parent != null) {
      var parent = path_to(ss.parent, {});
      parent[ss.path] = obj;
      return parent;
    } else {
      return obj;
    }
  };

  // HELPER | get the root of the specific state (i.e. either the data's root or the view's root)
  var root_of = function (ss) {
    var x = ss;
    while (x.parent != null) {
      x = x.parent;
    }
    return x;
  };

  // HELPER | remove all the children from the given DOM node
  var nuke = function (parent) {
    var last = parent.lastChild;
    while (last) {
      parent.removeChild(last);
      last = parent.lastChild;
    }
  };

  // HELPER: debounce the given functional when rapid function spread is expected
  var debounce = function (ms, foo) {
    var status = {inflight: false, timeout: null};
    status.inflight = false;
    return function () {
      if (!status.inflight) {
        status.inflight = true;
        status.timeout = window.setTimeout(function () {
          status.inflight = false;
          status.timeout = null;
          foo();
        }, ms /* ms */); // debounce the parameters
      }
    };
  };

  // HELPER | prepare a free unsubscribe object
  var make_unsub = function () {
    return {
      __data: function () {
      }, __view: function () {
      }
    };
  };

  // HELPER | augment an existing object with unsubscribe data
  var add_unsub = function (obj) {
    obj.__data = function () {
    };
    obj.__view = function () {
    };
  };

  // HELPER | fire the unsubscribe calls within an object
  var fire_unsub = function (unsub) {
    unsub.__data();
    unsub.__view();
  };

  // HELPER | subscribe to state and populate unsubscribe object
  var subscribe_state = function (state, unsub) {
    if (state.data != null) {
      unsub.__data = state.data.tree.subscribe(root_of(state.data).delta);
    } else {
      unsub.__data = function () {
      };
    }
    if (state.view != null) {
      unsub.__view = state.view.tree.subscribe(root_of(state.view).delta);
    } else {
      unsub.__view = function () {
      };
    }
  };

  var subscribe_view = function (state, unsub) {
    if (state.view != null) {
      unsub.__view = state.view.tree.subscribe(root_of(state.view).delta);
    } else {
      unsub.__view = function () {
      };
    }
  };

  // RUNTIME | Switch to the view object
  self.pV = function (state) {
    return {service: state.service, data: state.data, view: state.view, current: "view"};
  };

  // RUNTIME | Switch to the data object
  self.pD = function (state) {
    return {service: state.service, data: state.data, view: state.view, current: "data"};
  };

  // RUNTIME | Switch to the root object / (Root)
  self.pR = function (state) {
    var next = {service: state.service, data: state.data, view: state.view, current: state.current};
    var prior = next[state.current];
    while (prior.parent != null) {
      if (prior.parent == null) {
        next[state.current];
        return prior;
      }
      prior = prior.parent;
    }
    next[state.current] = prior;
    return next;
  };

  // RUNTIME | ../ (Up)
  self.pU = function (state) {
    var next = {service: state.service, data: state.data, view: state.view, current: state.current};
    var prior = next[state.current];
    if (prior.parent != null) {
      next[state.current] = prior;
    }
    return next;
  };

  // RUNTIME | dive one level Into path1/path2/..../pathN
  self.pI = function (state, name) {
    var prior = state[state.current];
    if (!(name in prior.delta)) {
      prior.delta[name] = {};
    }
    var next = {service: state.service, data: state.data, view: state.view, current: state.current};
    next[state.current] = {
      tree: prior.tree,
      delta: prior.delta[name],
      parent: prior,
      path: name
    };
    if (next.current == "data") {
      next.data.connection = prior.connection;
    }
    return next;
  };

  // RUNTIME | extend the given state with the view having a seperate child
  self.pEV = function (state, name) {
    if (!(name in state.view.delta)) {
      state.view.delta[name] = {};
    }
    return {
      service: state.service,
      data: state.data,
      view: {
        tree: state.view.tree,
        delta: state.view.delta[name],
        parent: state.view,
        path: name
      },
      current: state.current
    };
  };

  var fork = function (priorState) {
    var state = {
      service: priorState.service,
      data: new_delta_copy(priorState.data),
      view: new_delta_copy(priorState.view),
      current: priorState.current
    };
    if (state.data != null) {
      state.data.connection = priorState.data.connection;
    }
    return state;
  };

  // RUNTIME | subscribe between the state and the object.
  // When the member field within state of name changes, copy that value into the obj and run recompute()
  self.Y = function (state, obj, name, recompute) {
    var sub = function (value) {
      obj[name] = value;
      recompute();
    };
    subscribe(state, name, sub);
  };

  // RUNTIME | Just subscribee value to the object field of name (no-recompute)
  self.YS = function (state, obj, name) {
    var sub = function (value) {
      obj[name] = value;
    };
    subscribe(state, name, sub);
  };

  // RUNTIME | "Text"
  self.T = function (tx) {
    return document.createTextNode(tx);
  };

  // RUNTIME | <lookup path=...>
  self.L = function (state, name) {
    var dom = document.createTextNode("");
    var sub = function (value) {
      dom.nodeValue = value;
    };
    subscribe(state, name, sub);
    return dom;
  };

  // RUNTIME | <lookup path=... transform="$transform" />
  self.LT = function (state, name, transform) {
    var dom = document.createTextNode("");
    var sub = function (value) {
      dom.nodeValue = transform(value);
    };
    subscribe(state, name, sub);
    return dom;
  };

  // RUNTIME | <tag>
  self.E = function (tag, ns) {
    if (ns == undefined || ns == null) {
      return document.createElement(tag);
    } else {
      var result = document.createElementNS(ns, tag);
      result.setAttribute("xmlns", ns);
      return result;
    }
  };

  // RUNTIME | <pick name=...>
  self.P = function (parent, priorState, rxObj, childMaker) {
    var unsub = make_unsub();
    rxObj.__ = function () {
      if (this.name == rxObj.name) {
        return;
      }
      fire_unsub(unsub);
      this.name = rxObj.name;
      nuke(parent);
      var co = get_connection_obj(rxObj.name);
      var state = {
        service: priorState.service,
        data: {connection: co, tree: co.tree, delta: {}, parent: null, path: null},
        view: new_delta_copy(priorState.view),
        current: "data"
      };
      childMaker(state);
      subscribe_state(state, unsub);
    }.bind({name: ""});
  };

  // RUNTIME | <template name="...">
  self.TP = function (name, foo) {
    templates[name] = foo;
  };

  // RUNTIME | <... href="" ...>
  self.HREF = function (dom, href) {
    dom.setAttribute("href", fixHref(href));
    dom.onclick = function (evt) {
      var parts = (href.startsWith("/") ? href.substring(1) : href).split("/");
      if (route(parts, 0, router, {})) {
        evt.preventDefault();
        self.run(document.body, href, true);
        return false;
      }
      return true;
    };
  };
  // RUNTIME | <... class="" ...>
  self.ACLASS = function (dom, value) {
    dom.setAttribute("class", value);
  };
  // RUNTIME | <... src="" ...>
  self.ASRC = function (dom, value) {
    dom.setAttribute("src", value);
  };

  // RUNTIME | <tag rx:template=$name>
  self.UT = function (parent, state, name, child_maker) {
    var foo = templates[name];
    foo(parent, state, child_maker);
  };

  // RUNTIME | <tag rx:switch=path ..>
  self.SW = function (parent, priorState, name, childrenMaker) {
    var swst = {prior: null};
    add_unsub(swst);
    var sub = function (value) {
      if (value == this.prior) {
        return;
      }
      this.prior = value;
      fire_unsub(this);
      nuke(parent);
      var state = fork(priorState);
      childrenMaker(parent, state, "" + value);
      subscribe_state(state, this);

    }.bind(swst);
    subscribe(priorState, name, sub);
  };

  // RUNTIME | <tag rx:iterate=path ...>
  self.IT = function (parentDom, state, name, expandView, maker) {
    var it_state = self.pI(state, name);
    var domByKey = {};
    var viewUnSubByKey = {};

    var sub = {
      "+": function (key) {
        // TODO: view propagates don't work here
        var new_state = self.pI(it_state, key);
        if (expandView) {
          new_state = self.pEV(it_state, key);
        }
        new_state = {
          service: new_state.service,
          data: new_state.data,
          view: new_delta_copy(new_state.view),
          current: new_state.current
        };

        var unsub = make_unsub();
        var dom = maker(new_state);
        domByKey[key] = dom;
        viewUnSubByKey[key] = unsub;
        parentDom.append(dom);
        subscribe_view(new_state, unsub);
        return new_state[new_state.current].delta;
      },
      "-": function (key) {
        if (key in domByKey) {
          parentDom.removeChild(domByKey[key]);
          delete domByKey[key];
        }
        if (key in viewUnSubByKey) {
          fire_unsub(viewUnSubByKey[key]);
          delete viewUnSubByKey[key];
        }
      },
      "~": function (ord) {
        nuke(parentDom);
        for (var k = 0; k < ord.length; k++) {
          parentDom.append(domByKey[ord[k]]);
        }
      }
    };
    subscribe(state, name, sub);
  };

  var find = function (state, channel, key, value) {
    if (channel in state["data"].connection.outstanding) {
      var arr = state["data"].connection.outstanding[channel].options;
      for (var k = 0; k < arr.length; k++) {
        var s = arr[k];
        if (key in s && s[key] == value) {
          return s;
        }
      }
      return null;
    } else {
      return null;
    }
  };

  var customs = {};

  self.PRCUAC = function (name, foo) {
    customs[name] = foo;
  };

  self.exCC = function (dom, type, state, customCommandName) {
    dom.addEventListener(type, function () {
      if (customCommandName in customs) {
        customs[customCommandName]();
      }
    });
  };

  self.aCC = function (form, state, customCommandName, statusVar) {
    var signal = make_failure_signal(state, statusVar);
    form.onsubmit = function (evt) {
      if (customCommandName in customs) {
        evt.preventDefault();
        var obj = get_form(form);
        customs[customCommandName](obj, state, signal, self);
      }
    };
  };

  self.exD = function (dom, type, state, name, channel, key) {
    var decide = {value: null};
    dom.addEventListener(type, function () {
      var result = find(state, channel, key, decide.value);
      if (result != null) {
        let start = performance.now();
        state.data.connection.ptr.send(channel, result, {
          failure: function (reason) {

          },
          success: function (payload) {
            console.log("Success|" + payload.seq + ";latency=" + (performance.now() - start));
          }
        });
      }
    });
    subscribe(state, name, function (value) {
      decide.value = value;
    });
  };

  // RUNTIME: <tag .. rx:event="... set:name=value ...">
  self.onS = function (dom, type, state, name, value) {
    var runnable = function () {
      var obj = {};
      if (typeof (value) == "function") {
        obj[name] = value();
      } else {
        obj[name] = value;
      }
      var delta = path_to(state, obj);
      state[state.current].tree.update(delta);
    };
    if (type == "load") {
      window.setTimeout(runnable, 1);
    } else {
      dom.addEventListener(type, runnable);
    }
  };

  // RUNTIME: <tag .. rx:event="... toggle:name ...">
  self.onT = function (dom, type, state, name) {
    var captured = {value: false};
    dom.addEventListener(type, function () {
      var obj = {};
      obj[name] = !captured.value;
      var delta = path_to(state, obj);
      state[state.current].tree.update(delta);
    });
    subscribe(state, name, function (value) {
      captured.value = value == true;
    });
  };

  // RUNTIME: <tag .. rx:event="... delta:name=diff" ...">
  self.onD = function (dom, type, state, name, diff) {
    var captured = {value: 0};
    dom.addEventListener(type, function () {
      var obj = {};
      obj[name] = captured.value + diff;
      var delta = path_to(state, obj);
      state[state.current].tree.update(delta);
    });
    subscribe(state, name, function (value) {
      if (typeof (value) == "number") {
        captured.value = value;
      } else {
        var val = parseFloat(value);
        if (!isNaN(val)) {
          captured.value = val;
        }
      }
    });
  };

  self.DE = function (parent, priorState, evalState, channel, key, name, shouldBe, _expand, makerTrue, makerFalse) {
    var decide = {
      value: "",
      owner: parent,
      shown: false,
      eval: null
    };
    add_unsub(decide);
    var change = function (show) {
      fire_unsub(decide);
      nuke(parent);
      var state = fork(priorState);
      if (show === shouldBe) {
        makerTrue(parent, state);
      } else {
        makerFalse(parent, state);
      }
      subscribe_state(state, decide);
    };

    decide.update = function () {
      var eval = find(priorState, channel, key, decide.value) != null;
      if (decide.eval != eval) {
        decide.eval = eval;
        change(decide.eval);
      }
    };

    priorState.data.connection.subscribe(channel, function () {
      decide.update();
    });

    subscribe(evalState, name, function (value) {
      decide.value = value;
      decide.update();
    });
  };

  self.IF = function (parent, priorState, name, shouldBe, expandView, makerTrue, makerFalse) {
    var unsub = make_unsub();
    var set = function (value) {
      var show = (value ? true : false) === shouldBe;
      if (this.shown == show) {
        return;
      }
      this.shown = show;
      nuke(parent);
      fire_unsub(unsub);
      var state = fork(priorState);
      var next = state;
      if (typeof (value) == "object") {
        next = self.pI(next, name);
        if (expandView) {
          next = self.pEV(next, name);
        }
      }
      if (show) {
        makerTrue(parent, next);
      } else {
        makerFalse(parent, next);
      }
      subscribe_state(state, unsub);
    }.bind({shown: shouldBe});
    set(!shouldBe);
    subscribe(priorState, name, set);
  };

  /// RUNTIME | rx:action=copy:path
  self.aCP = function (form, state, name) {
    form.onsubmit = function (evt) {
      evt.preventDefault();
      var obj = get_form(form);
      if (name != "." && name != "") {
        var no = {};
        no[name] = obj;
        obj = no;
      }
      var delta = path_to(state, obj);
      state.view.tree.update(delta);
    };
  };

  // RUNTIME | <input ... rx:sync=path ...>
  self.SY = function (el, state, name, ms) {
    var type = ("type" in el) ? el.type.toUpperCase() : "text";
    var signal = function (value) {
      var obj = {};
      obj[name] = el.value;
      var delta = path_to(state, obj);
      state.view.tree.update(delta);
    };
    if (type == "CHECKBOX") {
      el.onchange = debounce(ms, function (evt) {
        signal(el.checked ? true : false);
      });
    } else if (type == "RADIO") {
      el.onchange = debounce(ms, function (evt) {
        if (el.checked) {
          signal(el.value);
        }
      });
    } else {
      el.onchange = debounce(ms, function (evt) {
        signal(el.value);
      });
      el.onkeyup = el.onchange;
      window.setTimeout(function () {
        signal(el.value);
      }, 1);
    }
  };

  // HELPER | extract all the inputs from the given element and build an object
  var build_obj = function (el, obj) {
    if (el.tagName.toUpperCase() == "TEXTAREA") {
      obj[el.name] = el.value;
    } else if (el.tagName.toUpperCase() == "SELECT") {
      obj[el.name] = el.value;
    } else if (el.tagName.toUpperCase() == "INPUT") {
      var type = ("type" in el) ? el.type.toUpperCase() : "text";
      if (type == "SUBMIT" || type == "RESET") return;
      if ("name" in el) {
        var name = el.name;
        var insertAt = obj;
        var push = name.endsWith("[]");
        if (push) {
          name = name.substr(0, name.length - 2);
          if (name in insertAt) {
            insertAt[name] = [];
          }
          insertAt = insertAt[name];
          if (type == "CHECKBOX") {
            insertAt.push(el.checked ? true : false);
          } else if (type == "RADIO") {
            if (el.checked) {
              insertAt.push(el.value);
            }
          } else {
            insertAt.push(el.value);
          }
        } else {
          if (type == "CHECKBOX") {
            insertAt[name] = el.checked ? true : false;
          } else if (type == "RADIO") {
            if (el.checked) {
              insertAt[name] = el.value;
            }
          } else {
            insertAt[name] = el.value;
          }
        }
      }
    } else {
      if ("children" in el) {
        var arr = el.children;
        var n = arr.length;
        for (var k = 0; k < n; k++) {
          var ch = el.children[k];
          build_obj(ch, obj);
        }
      }
    }
  };

  // HELPER | return an object of all the inputs of the given form element
  var get_form = function (form) {
    var obj = {};
    build_obj(form, obj);
    return obj;
  };

  // HELPER | create a signal for when things fail; this will write into the the view
  var make_failure_signal = function (state, failureVar) {
    return function (fail) {
      var obj = {};
      obj[failureVar] = fail;
      var delta = path_to(self.pV(state), obj);
      console.log(delta);
      state.view.tree.update(delta);
    };
  };

  /**
   /$$$$$$$$ /$$$$$$  /$$$$$$$   /$$$$$$
   |__  $$__//$$__  $$| $$__  $$ /$$__  $$
   | $$  | $$  \ $$| $$  \ $$| $$  \ $$
   | $$  | $$  | $$| $$  | $$| $$  | $$
   | $$  | $$  | $$| $$  | $$| $$  | $$
   | $$  | $$  | $$| $$  | $$| $$  | $$
   | $$  |  $$$$$$/| $$$$$$$/|  $$$$$$/
   |__/   \______/ |_______/  \______/
   */

  // RUNTIME | register the page for the uri to the given foo().
  self.PG = function (uri, foo) {
    var head = router;
    for (var k = 0; k < uri.length; k++) {
      var part = uri[k];
      if (!(part in head)) {
        head[part] = {};
      }
      head = head[part];
    }
    head["@"] = foo;
  };

  var route = function (parts, at, head, view) {
    if (at < parts.length) {
      if ("number" in head) {
        var neck = head["number"];
        var val = parseFloat(parts[at]);
        if (!isNaN(val)) {
          for (var branch in neck) {
            view[branch] = val;
            var candidate = route(parts, at + 1, neck[branch], view);
            if (candidate !== null) {
              return candidate;
            }
            delete view[branch];
          }
        }

      }
      if ("text" in head) {
        var neck = head["text"];
        var val = parts[at];
        for (var branch in neck) {
          view[branch] = val;
          var candidate = route(parts, at + 1, neck[branch], view);
          if (candidate !== null) {
            return candidate;
          }
          delete view[branch];
        }
      }
      if ("fixed" in head) {
        var neck = head["fixed"];
        for (var branch in neck) {
          if (branch == parts[at]) {
            var candidate = route(parts, at + 1, neck[branch], view);
            if (candidate !== null) {
              return candidate;
            }
          }
        }
      }
    } else {
      if ("@" in head) {
        return head["@"];
      }
    }
  };

  self.goto = function (viewState, uri) {
    // TODO: figure out a better model.
    window.setTimeout(function () {
      if (uri.startsWith("/")) {
        self.run(document.body, uri, true);
      } else {
        window.location.href = fixHref(uri);
      }
    }, 10);
  };

  self.init = function () {
    self.run(document.body, fixPath(window.location.pathname + window.location.hash), false);
    window.onpopstate = function () {
      console.log("popstate:" + fixPath(window.location.pathname));
      self.run(document.body, fixPath(window.location.pathname + window.location.hash), false);
    };
  };

  // API | Run the page in the given place
  self.run = function (where, path, push) {
    for (conKey in connections) {
      connections[conKey].tree.nuke();
    }
    var parts = (path.startsWith("/") ? path.substring(1) : path).split("/");
    var init = {"session_id": "R" + Math.random()};
    var foo = route(parts, 0, router, init);
    if (foo != null) {
      nuke(where);
      var state = {service: connection, data: null, view: fresh(where), current: "view"};
      foo(where, state);
      state.view.tree.subscribe(state.view.delta);
      state.view.tree.update(init);
      if (push) {
        window.history.pushState({}, "", fixHref(path));
      }
    } else {
      if (path != "/404") {
        self.run(where, "/404");
      } else {
        // default 404
      }
    }
  };

  var identities = {};

  self.SIGNOUT = function () {
    identities = {};
    localStorage.removeItem("identity_default");
    var axe = [];
    for (var cid in connections) {
      var co = connections[cid];
      if (co.ptr != null) {
        co.ptr.end();
      }
      axe.push(cid);
    }
    for (var k = 0; k < axe.length; k++) {
      delete connections[axe[k]];
    }

    self.goto(null, "/");
  };

  self.GOOGLE_SIGN_ON = function (accessToken) {
    connection.InitConvertGoogleUser(accessToken, {
      success: function (payload) {
        identities["default"] = payload.identity;
        localStorage.setItem("identity_default", payload.identity);
        self.goto(null, "/");
      },
      failure: function (reason) {
        console.log("Google failure: " + reason);
      }
    });
  };

  /** for custom elements to learn of the identity */
  self.ID = function (identityName, redirectTo) {
    if (identityName === true) {
      identityName = "default";
    }
    // IF identity contains dots
    var identity = null;
    var cleanup = function () {
    };

    var val = localStorage.getItem("identity_" + identityName);
    if (val) {
      identities[identityName] = val;
    }

    if (identityName.startsWith("direct:")) {
      // Use, as is
      identity = identityName.substr(7);
      console.log("toUse:" + identity);
    } else if (identityName in identities) {
      identity = identities[identityName];
      cleanup = function () {
        delete identities[identityName];
        localStorage.removeItem("identity_" + identityName);
      };
    } else {
      // whatever page we are, needs to die which means we need to nuke everything!
      window.setTimeout(function () {
        // TODO: this assumes a full app goes to the window
        self.goto(null, redirectTo);
      }, 10);
      return {abort: true};
    }

    return {abort: false, cleanup: cleanup, identity: identity};
  };

  var customDataSources = {};

  /** provide custom data */
  self.PRCUDA = function (name, foo) {
    customDataSources[name] = foo;
  };

  // <customdata src=src (parameter:x=y)* >
  self.CUDA = function (parent, priorState, src, rxobj, redirectTo, childMaker) {
    var unsub = make_unsub();
    rxobj.__ = debounce(10, function () {
      fire_unsub(unsub);
      nuke(parent);
      var customTree = false;
      if (src in customDataSources) {
        var cons = customDataSources[src];
        if (typeof (cons) == "function") {
          customTree = cons(rxobj, priorState, redirectTo, self);
        }
      }
      if (!customTree) {
        customTree = new AdamaTreeSimple();
      }
      var state = {
        service: priorState.service,
        data: {connection: priorState.connection, tree: customTree, delta: {}, parent: null, path: null},
        view: new_delta_copy(priorState.view),
        current: "data"
      };
      childMaker(state);
      subscribe_state(state, unsub);
    });
  };

  // <title value="..." />
  self.ST = function (rxobj) {
    document.title = rxobj.value;
    rxobj.__ = debounce(1, function () {
      document.title = rxobj.value;
    });
  };

  // <connection ...>
  self.CONNECT = function (state, rxobj, redirectTo) {
    var unsub = {
      view: function () {
      }
    };
    rxobj.__ = debounce(10, function () {
      var co = get_connection_obj(rxobj.name);
      var desired = rxobj.space + "/" + rxobj.key;
      var bind = function (sendNow) {
        co.space = rxobj.space;
        co.key = rxobj.key;
        unsub.view = state.view.tree.subscribe(function () {
          if (co.ptr == null) {
            return;
          }
          co.ptr.update(state.view.tree.copy(), {
            success: function () {
            },
            failure: function () {
            }
          });
        });
        if (sendNow) {
          console.log(state.view.tree.str());
          co.ptr.update(state.view.tree.copy(), {
            success: function () {
            },
            failure: function () {
            }
          });
        }
      };
      if (co.ptr != null && co.bound == desired) {
        bind(true);
        return;
      }
      if (co.ptr != null) {
        co.ptr.end();
      }
      co.bound = desired;
      var idLookup = self.ID(rxobj.identity, redirectTo);
      if (idLookup.abort) {
        return;
      }
      var identity = idLookup.identity;
      var cleanup = idLookup.cleanup;
      unsub.view();
      var vw = state.view.tree.copy();
      co.ptr = connection.ConnectionCreate(identity, rxobj.space, rxobj.key, vw, {
        next: function (payload) {
          if ("data" in payload.delta) {
            co.tree.update(payload.delta.data);
          }
          if ("outstanding" in payload.delta) {
            co.ondecide(payload.delta.outstanding);
          }
        },
        failure: function (reason) {
          // TODO: if not authorized
          /*
          if (false) {
            cleanup();
          }
          */
          co.ptr = null;
          // TODO: schedule a retry? invoke:  rxobj.__();
        }
      });
      bind(false);
    });
  };

  self.INTERNAL = function (priorState) {
    return {
      service: priorState.service,
      data: {connection: null, tree: new AdamaTreeSimple(), delta: {}, parent: null, path: null},
      view: new_delta_copy(priorState.view),
      current: "data"
    };
  };

  var recall_email = function (el) {
    if (el.tagName.toUpperCase() == "INPUT") {
      if ("email" == el.type && "email" == el.name) {
        el.value = localStorage.getItem("email_remember");
      }
    } else {
      if ("children" in el) {
        var arr = el.children;
        var n = arr.length;
        for (var k = 0; k < n; k++) {
          recall_email(el.children[k]);
        }
      }
    }
  };

  // RUNTIME | rx:action=adama:sign-in
  self.aSO = function (form, state, identityName, failureVar, forwardTo) {
    var signal = make_failure_signal(state, failureVar);
    window.setTimeout(function () {
      recall_email(form);
    }, 1);
    form.onsubmit = function (evt) {
      evt.preventDefault();
      var req = get_form(form);
      if (req.remember) {
        localStorage.setItem("email_remember", req.email);
      } else {
        localStorage.setItem("email_remember", "");
      }
      connection.AccountLogin(req.email, req.password, {
        success: function (payload) {
          signal(false);
          identities[identityName] = payload.identity;
          localStorage.setItem("identity_" + identityName, payload.identity);
          self.goto(state.view, forwardTo);
        },
        failure: function (code) {
          signal(true);
          // TODO: sort out console logging
          console.log("FAILURE:" + code);
        }
      });
    };
  };

  // RUNTIME | rx:action=adama:sign-up
  self.aSU = function (form, state, failureVar, forwardTo) {
    var signal = make_failure_signal(state, failureVar);
    form.onsubmit = function (evt) {
      evt.preventDefault();
      var req = get_form(form);
      connection.InitSetupAccount(req.email, {
        success: function (payload) {
          signal(false);
          localStorage.setItem("email", req.email);
          self.goto(state.view, forwardTo);
        },
        failure: function (code) {
          signal(true);
          console.log("FAILURE:");
        }
      });
    };
  };

  // RUNTIME | rx:action=adama:set-password
  self.aSP = function (form, state, failureVar, forwardTo) {
    var signal = make_failure_signal(state, failureVar);
    form.onsubmit = function (evt) {
      evt.preventDefault();
      var req = get_form(form);
      if (!("email" in req)) {
        req.email = localStorage.getItem("email");
      }
      connection.InitCompleteAccount(req.email, false, req.code, {
        success: function (init) {
          console.log(init);
          var identity = init.identity;
          connection.AccountSetPassword(init.identity, req.password, {
            success: function () {
              signal(false);
              localStorage.setItem("identity_default", identity);
              self.goto(state.view, forwardTo);
            },
            failure: function (reason) {
              signal(true);
            }
          });

        },
        failure: function (reason) {
          signal(true);
        }
      });
    };
  };

  // RUNTIME | rx:action=send:$channel
  self.aSD = function (form, state, channel, failureVar) {
    var signal = make_failure_signal(state, failureVar);
    form.onsubmit = function (evt) {
      evt.preventDefault();
      var start = performance.now();
      state.data.connection.ptr.send(channel, get_form(form), {
        success: function (payload) {
          signal(false);
          console.log("Success|" + payload.seq + ";latency=" + (performance.now() - start)); // TODO: graph it
        },
        failure: function (reason) {
          signal(true);
          console.log("FAILURE TO SEND: " + reason); // TODO: log it
        }
      });
    };
  };

  window.rxhtml = self;
  return self;
})();