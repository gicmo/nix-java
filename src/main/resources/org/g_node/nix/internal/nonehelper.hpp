// Copyright © 2016 German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.
//
// Author: Christian Kellner <kellner@bio.lmu.de>

#ifndef NIX_JAVA_NONE_H
#define NIX_JAVA_NONE_H

#include <nix/None.hpp>

namespace nix_java {

static inline void *get_none_pointer() {
    return (void *) &(nix::none);
}

}


#endif // NIX_JAVA_NONE_H

