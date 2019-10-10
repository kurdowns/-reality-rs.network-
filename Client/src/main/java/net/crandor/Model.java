package net.crandor;

import java.io.PrintWriter;

public class Model extends Entity {

	private int boundsType;
	public int ambient;
	public int contrast;
    public static boolean objectExists;
    public static int currentCursorX;// cursorXPos
    public static int currentCursorY;// cursorYPos
    public static int objectsRendered;
    public static long objectsInCurrentRegion[] = new long[1000];
    static byte[][] modelHeader;
    static boolean hasAnEdgeToRestrict[] = new boolean[8000];
    static int projected_vertex_x[] = new int[8000];
    static int projected_vertex_y[] = new int[8000];
    static int projected_vertex_z[] = new int[8000];
    static int camera_vertex_y[] = new int[8000];
    static int camera_vertex_x[] = new int[8000];
    static int camera_vertex_z[] = new int[8000];
    static Model objectModel = new Model();
    static Model npcModel = new Model();
    static Model spotAnimModel = new Model();
    static Model interfaceModel = new Model();
    static int anInt1681;
    static int anInt1682;
    static int anInt1683;
    private static int[] anIntArray2007 = new int[128];
    private static int anIntArray1622[] = new int[2000];
    private static int anIntArray1623[] = new int[2000];
    private static int anIntArray1624[] = new int[2000];
    private static byte anIntArray1625[] = new byte[2000];
    private static short anIntArray1626[] = new short[2000];

    static {
    	int i = 0;
    	int i_0_ = 248;
    	while (i < 9)
    	    anIntArray2007[i++] = 255;
    	while (i < 16) {
    	    anIntArray2007[i++] = i_0_;
    	    i_0_ -= 8;
    	}
    	while (i < 32) {
    	    anIntArray2007[i++] = i_0_;
    	    i_0_ -= 4;
    	}
    	while (i < 64) {
    	    anIntArray2007[i++] = i_0_;
    	    i_0_ -= 2;
    	}
    	while (i < 128)
    	    anIntArray2007[i++] = i_0_--;
    }

    public int modelFormat = 0;
    public byte texture_coordinates[]; // !texturePointers
    public byte texture_render_type[]; // !textureDrawTypes
    public short face_texture[]; // !textureBackground // textureContainer
    public boolean force_texture;
    // public Class34[] triangle_light;
    public boolean display_model_specific_texture;
    public int numberOfVerticeCoordinates;// vertexCount
    public int verticesXCoordinate[];// verticesXCoordinate
    public int verticesYCoordinate[];// verticesYCoordinate
    public int verticesZCoordinate[];// verticesZCoordinate
    public int numberOfTriangleFaces;// triangleCount
    public int face_a[];// triangleA
    public int face_b[];// triangleB
    public int face_c[];// triangleC
    public int face_shade_a[];// triangleHslA
    public int face_shade_b[];// triangleHslB
    public int face_shade_c[];// triangleHslC
    public byte face_render_type[];// triangleDrawType
    public int face_render_priorities[];// facePriority
    public byte face_alpha[];// TriangleAlpha
    public short face_color[];// triangleColorOrTexture
    public int face_priority;
    public int numberOfTexturedFaces;// textureTriangleCount
    public int textured_face_a[];// trianglePIndex
    public int textured_face_b[];// triangleMIndex
    public int textured_face_c[];// triangleNIndex
    public int lowestX;// minX
    public int highestX;// maxX
    public int highestZ;// maxZ
    public int lowestZ;// minZ
    public int lowestY;// maxY
    public int anIntArray1655[];// vertexVSkin
    public int anIntArray1656[];// triangleTSkin
    public int vertexSkin[][];// vertexSkin
    public int triangleSkin[][];// triangleSkin
    public boolean rendersWithinOneTile;// oneSquareModel
    VertexNormal vertexNormalOffset[];// vertexNormalOffset
	VertexNormal vertexNormals[];
    private boolean scaledVertices = false;

    public Model(byte[] data) {
        if (data != null && data.length > 1) {
            if (data[data.length - 1] == -1 && data[data.length - 2] == -1) {
                processCurrentModelData(data);
            } else {
                process508ModelData(data);
            }
        }
    }

    private Model() {
        rendersWithinOneTile = false;
    }

    public Model(Model models[]) {
        this(models, 2);
    }

    public Model(Model attatch[], int number_of_models) {
        try {
            rendersWithinOneTile = false;
            boolean has_render_type = false;
            boolean has_priorities = false;
            boolean has_alpha = false;
            boolean has_skin = false;
            boolean has_texture = false;
            boolean has_coordinates = false;
            numberOfVerticeCoordinates = 0;
            numberOfTriangleFaces = 0;
            numberOfTexturedFaces = 0;
            face_priority = (byte) -1;
            Model connect;
            int model_index;
            for (model_index = 0; model_index < number_of_models; model_index++) {
                connect = attatch[model_index];
                if (connect != null) {
                    numberOfVerticeCoordinates += connect.numberOfVerticeCoordinates;
                    numberOfTriangleFaces += connect.numberOfTriangleFaces;
                    numberOfTexturedFaces += connect.numberOfTexturedFaces;
                    if (connect.face_render_priorities != null) {
                        has_priorities = true;
                    } else {
                        if (face_priority == -1)
                            face_priority = connect.face_priority;

                        if (face_priority != connect.face_priority)
                            has_priorities = true;
                    }
                    has_render_type |= connect.face_render_type != null;
                    has_alpha |= connect.face_alpha != null;
                    has_coordinates = has_coordinates | connect.texture_coordinates != null;
                    has_skin = has_skin | connect.anIntArray1656 != null;
                    has_texture = has_texture | connect.face_texture != null;
                }
            }
            face_color = new short[numberOfTriangleFaces];
            verticesXCoordinate = new int[numberOfVerticeCoordinates];
            verticesYCoordinate = new int[numberOfVerticeCoordinates];
            verticesZCoordinate = new int[numberOfVerticeCoordinates];
            anIntArray1655 = new int[numberOfVerticeCoordinates];
            face_a = new int[numberOfTriangleFaces];
            face_b = new int[numberOfTriangleFaces];
            face_c = new int[numberOfTriangleFaces];

            if (has_render_type)
                face_render_type = new byte[numberOfTriangleFaces];

            if (has_skin)
                anIntArray1656 = new int[numberOfTriangleFaces];

            if (numberOfTexturedFaces > 0) {
                textured_face_a = new int[numberOfTexturedFaces];
                textured_face_b = new int[numberOfTexturedFaces];
                textured_face_c = new int[numberOfTexturedFaces];
                texture_render_type = new byte[numberOfTexturedFaces];
            }

            if (has_coordinates)
                texture_coordinates = new byte[numberOfTriangleFaces];

            if (has_texture)
                face_texture = new short[numberOfTriangleFaces];

            if (has_alpha)
                face_alpha = new byte[numberOfTriangleFaces];

            if (has_priorities)
                face_render_priorities = new int[numberOfTriangleFaces];

            numberOfVerticeCoordinates = 0;
            numberOfTriangleFaces = 0;
            numberOfTexturedFaces = 0;
            for (model_index = 0; model_index < number_of_models; model_index++) {
                connect = attatch[model_index];
                if (connect != null) {
                    for (int j1 = 0; j1 < connect.numberOfTriangleFaces; j1++) {
                        if (has_render_type && connect.face_render_type != null) {
                        	face_render_type[numberOfTriangleFaces] = connect.face_render_type[j1];
                        }
						if (has_priorities) {
							if (connect.face_render_priorities != null) {
								face_render_priorities[numberOfTriangleFaces] = connect.face_render_priorities[j1];
							} else {
								face_render_priorities[numberOfTriangleFaces] = connect.face_priority;
							}
						}

                        if (has_alpha && connect.face_alpha != null)
                            face_alpha[numberOfTriangleFaces] = connect.face_alpha[j1];

                        if (has_texture) {
                            if (connect.face_texture != null) {
                                face_texture[numberOfTriangleFaces] = connect.face_texture[j1];
                            } else {
                                face_texture[numberOfTriangleFaces] = (short) -1;
                            }
                        }

                        if (has_alpha && connect.anIntArray1656 != null)
                            anIntArray1656[numberOfTriangleFaces] = connect.anIntArray1656[j1];

						face_a[numberOfTriangleFaces] = method465(connect, connect.face_a[j1]);
						face_b[numberOfTriangleFaces] = method465(connect, connect.face_b[j1]);
						face_c[numberOfTriangleFaces] = method465(connect, connect.face_c[j1]);
                        face_color[numberOfTriangleFaces] = connect.face_color[j1];
                        numberOfTriangleFaces++;
                    }
                }
            }
            int face = 0;
            for (model_index = 0; model_index < number_of_models; model_index++) {
                connect = attatch[model_index];
                if (connect != null) {
                	for (int mapped_pointers = 0; mapped_pointers < connect.numberOfTriangleFaces; mapped_pointers++) {
    					if (has_coordinates) {
    						texture_coordinates[face++] = (byte) (connect.texture_coordinates != null && connect.texture_coordinates[mapped_pointers] != -1 ? connect.texture_coordinates[mapped_pointers] + numberOfTexturedFaces : -1);
    					}
                	}
                    for (int texture_index = 0; texture_index < connect.numberOfTexturedFaces; texture_index++) {
                        byte opcode = (texture_render_type[numberOfTexturedFaces] = connect.texture_render_type[texture_index]);
						if (opcode == 0) {
							textured_face_a[numberOfTexturedFaces] = (short) method465(connect, connect.textured_face_a[texture_index]);
							textured_face_b[numberOfTexturedFaces] = (short) method465(connect, connect.textured_face_b[texture_index]);
							textured_face_c[numberOfTexturedFaces] = (short) method465(connect, connect.textured_face_c[texture_index]);
						}
                        if (opcode >= 1 && opcode <= 3) {
                        	textured_face_a[numberOfTexturedFaces] = connect.textured_face_a[texture_index];
                        	textured_face_b[numberOfTexturedFaces] = connect.textured_face_b[texture_index];
                        	textured_face_c[numberOfTexturedFaces] = connect.textured_face_c[texture_index];
                        }
                        numberOfTexturedFaces++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Model(Model model, boolean flag2, boolean flag, boolean flag1) {
        rendersWithinOneTile = false;
        numberOfVerticeCoordinates = model.numberOfVerticeCoordinates;
        numberOfTriangleFaces = model.numberOfTriangleFaces;
        numberOfTexturedFaces = model.numberOfTexturedFaces;
        if (flag2) {
            verticesXCoordinate = model.verticesXCoordinate;
            verticesYCoordinate = model.verticesYCoordinate;
            verticesZCoordinate = model.verticesZCoordinate;
        } else {
            verticesXCoordinate = new int[numberOfVerticeCoordinates];
            verticesYCoordinate = new int[numberOfVerticeCoordinates];
            verticesZCoordinate = new int[numberOfVerticeCoordinates];
            for (int j = 0; j < numberOfVerticeCoordinates; j++) {
                verticesXCoordinate[j] = model.verticesXCoordinate[j];
                verticesYCoordinate[j] = model.verticesYCoordinate[j];
                verticesZCoordinate[j] = model.verticesZCoordinate[j];
            }

        }
        if (flag) {
            face_color = model.face_color;
        } else {
            face_color = new short[numberOfTriangleFaces];
            for (int k = 0; k < numberOfTriangleFaces; k++) {
            	face_color[k] = model.face_color[k];
            }
        }

        if (flag1) {
            face_alpha = model.face_alpha;
        } else {
            face_alpha = new byte[numberOfTriangleFaces];
            if (model.face_alpha != null) {
                for (int i1 = 0; i1 < numberOfTriangleFaces; i1++)
                    face_alpha[i1] = model.face_alpha[i1];
            } else {
                for (int i1 = 0; i1 < numberOfTriangleFaces; i1++)
                    face_alpha[i1] = 0;
            }
        }
        anIntArray1655 = model.anIntArray1655;
        anIntArray1656 = model.anIntArray1656;
        if (numberOfTexturedFaces > 0) {
            textured_face_a = model.textured_face_a;
            textured_face_b = model.textured_face_b;
            textured_face_c = model.textured_face_c;
            face_texture = model.face_texture;
            texture_coordinates = model.texture_coordinates;
            texture_render_type = model.texture_render_type;
        }
        face_a = model.face_a;
        face_b = model.face_b;
        face_c = model.face_c;
        face_priority = model.face_priority;
        face_render_priorities = model.face_render_priorities;
        face_render_type = model.face_render_type;
    }

    public Model(boolean flag, boolean flag1, Model model) {
        rendersWithinOneTile = false;
        numberOfVerticeCoordinates = model.numberOfVerticeCoordinates;
        numberOfTriangleFaces = model.numberOfTriangleFaces;
        numberOfTexturedFaces = model.numberOfTexturedFaces;
        if (flag) {
            verticesYCoordinate = new int[numberOfVerticeCoordinates];
            for (int j = 0; j < numberOfVerticeCoordinates; j++) {
                verticesYCoordinate[j] = model.verticesYCoordinate[j];
            }
        } else {
            verticesYCoordinate = model.verticesYCoordinate;
        }

        if (numberOfTexturedFaces > 0) {
            textured_face_a = model.textured_face_a;
            textured_face_b = model.textured_face_b;
            textured_face_c = model.textured_face_c;
            face_texture = model.face_texture;
            texture_coordinates = model.texture_coordinates;
            texture_render_type = model.texture_render_type;
        }

        if (flag1) {
            face_shade_a = new int[numberOfTriangleFaces];
            face_shade_b = new int[numberOfTriangleFaces];
            face_shade_c = new int[numberOfTriangleFaces];
            for (int k = 0; k < numberOfTriangleFaces; k++) {
                face_shade_a[k] = model.face_shade_a[k];
                face_shade_b[k] = model.face_shade_b[k];
                face_shade_c[k] = model.face_shade_c[k];
            }

            face_render_type = new byte[numberOfTriangleFaces];
            if (model.face_render_type == null) {
                for (int l = 0; l < numberOfTriangleFaces; l++)
                    face_render_type[l] = 0;

            } else {
                for (int i1 = 0; i1 < numberOfTriangleFaces; i1++)
                    face_render_type[i1] = model.face_render_type[i1];

            }
            vertexNormals = new VertexNormal[numberOfVerticeCoordinates];
            for (int j1 = 0; j1 < numberOfVerticeCoordinates; j1++) {
                VertexNormal class33 = vertexNormals[j1] = new VertexNormal();
                VertexNormal class33_1 = model.vertexNormals[j1];
                class33.x = class33_1.x;
                class33.y = class33_1.y;
                class33.z = class33_1.z;
                class33.magnitude = class33_1.magnitude;
            }

            vertexNormalOffset = model.vertexNormalOffset;
        } else {
            face_shade_a = model.face_shade_a;
            face_shade_b = model.face_shade_b;
            face_shade_c = model.face_shade_c;
            face_render_type = model.face_render_type;
        }
        verticesXCoordinate = model.verticesXCoordinate;
        verticesZCoordinate = model.verticesZCoordinate;
        face_color = model.face_color;
        face_alpha = model.face_alpha;
        face_render_priorities = model.face_render_priorities;
        face_priority = model.face_priority;
        face_a = model.face_a;
        face_b = model.face_b;
        face_c = model.face_c;
        contrast = model.contrast;
        ambient = model.ambient;
    }

	public int getLowestX() {
		calculateDiagonals();
		return lowestX;
	}

    public static void nullLoader() {
        modelHeader = null;
        hasAnEdgeToRestrict = null;
        projected_vertex_y = null;
        camera_vertex_y = null;
        camera_vertex_x = null;
        camera_vertex_z = null;
    }

    public static void method460(byte abyte0[], int j) {
        // nomad maps model fix
        //if(j == 57650 || j == 2339) {
        //     return;
        // }

        try {
            if (abyte0 == null) {
                return;
            }
            modelHeader[j] = abyte0;
        } catch (Exception e) {
            System.out.println("error with model: " + j);
            e.printStackTrace();
        }
    }

    public static void initialize(int i) {
        modelHeader = new byte[i][];
    }

    public static void clearHeader(int j) {
        modelHeader[j] = null;
    }

    public static Model getModel(int model) {
        if (modelHeader == null)
            return null;
        byte[] class21 = modelHeader[model];
        if (class21 == null) {
            Client.instance.onDemandFetcher.method548(model);
            return null;
        } else {
            return new Model(class21);
        }
    }

    public static boolean isCached(int i) {
        if (modelHeader == null || i > modelHeader.length - 1)
            return false;

        byte[] class21 = modelHeader[i];
        if (class21 == null) {
            Client.instance.onDemandFetcher.method548(i);
            return false;
        } else {
            return true;
        }
    }

    public static boolean isCached(int i, boolean osrs) {
        if (modelHeader == null || i > modelHeader.length - 1)
            return false;

        byte[] class21 = modelHeader[i];
        if (class21 == null) {
            Client.instance.onDemandFetcher.method548(osrs ? 5 : 0, i);
            return false;
        } else {
            return true;
        }
    }

    public static final int method481(int hsl, int light, int flags) {
        if ((flags & 2) == 2) {
            if (light < 0)
                light = 0;
            else if (light > 127)
                light = 127;
            light = anIntArray2007[light];
            return light;
        }
        light = light * (hsl & 0x7f) >> 7;
        if (light < 2)
            light = 2;
        else if (light > 126)
            light = 126;
        return (hsl & 0xff80) + light;
    }

	Model copy(Model instance, boolean bool) {
		return copy(bool, instance);
	}

    /**
     * Copy a model instance to a new instance
     *
     * @param original
     * @return
     */
	public Model copy(boolean bool, Model model) {
		// vertexCount;
		model.numberOfVerticeCoordinates = numberOfVerticeCoordinates;
		// triangleCount
		model.numberOfTriangleFaces = numberOfTriangleFaces;
		// texTriangleCount
		model.numberOfTexturedFaces = numberOfTexturedFaces;

		if (model.verticesXCoordinate == null || model.verticesXCoordinate.length < numberOfVerticeCoordinates) {
			model.verticesXCoordinate = new int[numberOfVerticeCoordinates + 100];
			model.verticesYCoordinate = new int[numberOfVerticeCoordinates + 100];
			model.verticesZCoordinate = new int[numberOfVerticeCoordinates + 100];
		}
		for (int i = 0; i < numberOfVerticeCoordinates; i++) {
			model.verticesXCoordinate[i] = verticesXCoordinate[i];
			model.verticesYCoordinate[i] = verticesYCoordinate[i];
			model.verticesZCoordinate[i] = verticesZCoordinate[i];
		}

		if (bool) {
			model.face_alpha = face_alpha;
		} else {
			if (anIntArray1625.length < numberOfTriangleFaces)
				anIntArray1625 = new byte[Math.max(anIntArray1625.length * 2, numberOfTriangleFaces)];

			model.face_alpha = anIntArray1625;
			if (face_alpha == null) {
				for (int i = 0; i < numberOfTriangleFaces; i++) {
					model.face_alpha[i] = 0;
				}
			} else {
				for (int i = 0; i != numberOfTriangleFaces; i++) {
					model.face_alpha[i] = face_alpha[i];
				}
			}
		}

		model.face_render_type = face_render_type;
		model.face_color = face_color;
		model.face_render_priorities = face_render_priorities;
		model.face_priority = face_priority;
		model.triangleSkin = triangleSkin;
		model.vertexSkin = vertexSkin;
		model.face_a = face_a;
		model.face_b = face_b;
		model.face_c = face_c;
		model.face_texture = face_texture;
		model.face_shade_a = face_shade_a;
		model.face_shade_b = face_shade_b;
		model.face_shade_c = face_shade_c;
		model.textured_face_a = textured_face_a;
		model.textured_face_b = textured_face_b;
		model.textured_face_c = textured_face_c;
		model.texture_coordinates = texture_coordinates;
		model.texture_render_type = texture_render_type;
		model.boundsType = 0;

		return model;
	}

    public void processCurrentModelData(byte modelData[]) {
        Stream data = new Stream(modelData);
        Stream data_2 = new Stream(modelData);
        Stream data_3 = new Stream(modelData);
        Stream data_4 = new Stream(modelData);
        Stream data_5 = new Stream(modelData);
        Stream data_6 = new Stream(modelData);
        Stream data_7 = new Stream(modelData);
        data.currentOffset = modelData.length - 23;
        numberOfVerticeCoordinates = data.readUnsignedWord();
        numberOfTriangleFaces = data.readUnsignedWord();
        numberOfTexturedFaces = data.readUnsignedByte();
        int flags = data.readUnsignedByte();
        boolean has_fill_opcode = (flags & 0x1) != 0;
        boolean second_flag = (flags & 0x2) != 0;
        boolean has_vertex_normals = (flags & 0x4) != 0;
        boolean fourth_flag = (flags & 0x8) != 0;
        if (!fourth_flag) {
            process600ModelData(modelData);
            return;
        }
        if (fourth_flag) {
            data.currentOffset -= 7;
            modelFormat = data.readUnsignedByte();
            data.currentOffset += 6;
        }
        int priority = data.readUnsignedByte();
        int alpha_opcode = data.readUnsignedByte();
        int tSkin_opcode = data.readUnsignedByte();
        int texture_opcode = data.readUnsignedByte();
        int i3 = data.readUnsignedByte();
        int j3 = data.readUnsignedWord();
        int k3 = data.readUnsignedWord();
        int l3 = data.readUnsignedWord();
        int i4 = data.readUnsignedWord();
        int j4 = data.readUnsignedWord();
        int particle_index = 0;
        int l4 = 0;
        int particle_color = 0;

        if (numberOfTexturedFaces > 0) {
            texture_render_type = new byte[numberOfTexturedFaces];
            data.currentOffset = 0;
            for (int index = 0; index < numberOfTexturedFaces; index++) {
                byte opcode = texture_render_type[index] = data
                        .readSignedByte();
                if (opcode == 0)
                    particle_index++;
                if (opcode >= 1 && opcode <= 3)
                    l4++;
                if (opcode == 2)
                    particle_color++;
            }
        }
        int k5 = numberOfTexturedFaces;
        int vertexModOffset = k5;
        k5 += numberOfVerticeCoordinates;
        int drawTypeBasePos = k5;
        if (has_fill_opcode)
            k5 += numberOfTriangleFaces;
        int triMeshLinkOffset = k5;
        k5 += numberOfTriangleFaces;
        int facePriorityBasePos = k5;
        if (priority == 255)
            k5 += numberOfTriangleFaces;
        int tSkinBasePos = k5;
        if (tSkin_opcode == 1)
            k5 += numberOfTriangleFaces;
        int vSkinBasePos = k5;
        if (i3 == 1)
            k5 += numberOfVerticeCoordinates;
        int alphaBasePos = k5;
        if (alpha_opcode == 1)
            k5 += numberOfTriangleFaces;
        int triVPointOffset = k5;
        k5 += i4;
        int texturedTriangleTextureIDBasePos = k5;
        if (texture_opcode == 1)
            k5 += numberOfTriangleFaces * 2;
        int texturedTriangleIDBasePos = k5;
        k5 += j4;
        int triColorOffset = k5;
        k5 += numberOfTriangleFaces * 2;
        int vertexXOffset = k5;
        k5 += j3;
        int vertexYOffset = k5;
        k5 += k3;
        int vertexZOffset = k5;
        k5 += l3;
        int mainByteBufferOffset = k5;
        k5 += particle_index * 6;
        int firstByteBufferOffset = k5;
        k5 += l4 * 6;
        int i_59_ = 6;
        if (modelFormat != 14) {
            if (modelFormat >= 15) {
                i_59_ = 9;
            }
        } else {
            i_59_ = 7;
        }
        int secondByteBufferOffset = k5;
        k5 += i_59_ * l4;
        int thirdByteBufferOffset = k5;
        k5 += l4;
        int fourthByteBufferOffset = k5;
        k5 += l4;
        int fifthByteBufferOffset = k5;
        k5 += l4 + particle_color * 2;

        verticesXCoordinate = new int[numberOfVerticeCoordinates];
        verticesYCoordinate = new int[numberOfVerticeCoordinates];
        verticesZCoordinate = new int[numberOfVerticeCoordinates];
        face_a = new int[numberOfTriangleFaces];
        face_b = new int[numberOfTriangleFaces];
        face_c = new int[numberOfTriangleFaces];
        if (i3 == 1)
            anIntArray1655 = new int[numberOfVerticeCoordinates];
        if (has_fill_opcode)
            face_render_type = new byte[numberOfTriangleFaces];
        if (priority == 255)
            face_render_priorities = new int[numberOfTriangleFaces];
        if (alpha_opcode == 1)
            face_alpha = new byte[numberOfTriangleFaces];
        if (tSkin_opcode == 1)
            anIntArray1656 = new int[numberOfTriangleFaces];
        if (texture_opcode == 1)
            face_texture = new short[numberOfTriangleFaces];
        if (texture_opcode == 1 && numberOfTexturedFaces > 0)
            texture_coordinates = new byte[numberOfTriangleFaces];
        face_color = new short[numberOfTriangleFaces];
        if (numberOfTexturedFaces > 0) {
            textured_face_a = new int[numberOfTexturedFaces];
            textured_face_b = new int[numberOfTexturedFaces];
            textured_face_c = new int[numberOfTexturedFaces];
        }
        data.currentOffset = vertexModOffset;
        data_2.currentOffset = vertexXOffset;
        data_3.currentOffset = vertexYOffset;
        data_4.currentOffset = vertexZOffset;
        data_5.currentOffset = vSkinBasePos;
        int l10 = 0;
        int i11 = 0;
        int j11 = 0;
        for (int k11 = 0; k11 < numberOfVerticeCoordinates; k11++) {
            int l11 = data.readUnsignedByte();
            int j12 = 0;
            if ((l11 & 1) != 0)
                j12 = data_2.method421();
            int l12 = 0;
            if ((l11 & 2) != 0)
                l12 = data_3.method421();
            int j13 = 0;
            if ((l11 & 4) != 0)
                j13 = data_4.method421();
            verticesXCoordinate[k11] = l10 + j12;
            verticesYCoordinate[k11] = i11 + l12;
            verticesZCoordinate[k11] = j11 + j13;
            l10 = verticesXCoordinate[k11];
            i11 = verticesYCoordinate[k11];
            j11 = verticesZCoordinate[k11];
            if (anIntArray1655 != null)
                anIntArray1655[k11] = data_5.readUnsignedByte();
        }
        data.currentOffset = triColorOffset;
        data_2.currentOffset = drawTypeBasePos;
        data_3.currentOffset = facePriorityBasePos;
        data_4.currentOffset = alphaBasePos;
        data_5.currentOffset = tSkinBasePos;
        data_6.currentOffset = texturedTriangleTextureIDBasePos;
        data_7.currentOffset = texturedTriangleIDBasePos;
        for (int triangle = 0; triangle < numberOfTriangleFaces; triangle++) {
            face_color[triangle] = (short) data.readUnsignedWord();
            if (has_fill_opcode) {
                face_render_type[triangle] = data_2.readSignedByte();
            }
            if (priority == 255)
                face_render_priorities[triangle] = data_3.readSignedByte();

            if (alpha_opcode == 1) {
                face_alpha[triangle] = data_4.readSignedByte();
            }

            if (tSkin_opcode == 1)
                anIntArray1656[triangle] = data_5.readUnsignedByte();

            if (texture_opcode == 1) {
                face_texture[triangle] = (short) (data_6.readUnsignedWord() - 1);
            }
            if (texture_coordinates != null) {
                if (face_texture[triangle] == -1) {
                    texture_coordinates[triangle] = -1;
                } else {
                    texture_coordinates[triangle] = (byte) (data_7
                            .readUnsignedByte() - 1);
                }
            }
        }
        data.currentOffset = triVPointOffset;
        data_2.currentOffset = triMeshLinkOffset;
        short coordinate_a = 0;
        short coordinate_b = 0;
        short coordinate_c = 0;
        int last_coordinate = 0;
        for (int face = 0; face < numberOfTriangleFaces; face++) {
            int opcode = data_2.readUnsignedByte();
            if (opcode == 1) {
                coordinate_a = (short) (data.method421() + last_coordinate);
                last_coordinate = coordinate_a;
                coordinate_b = (short) (data.method421() + last_coordinate);
                last_coordinate = coordinate_b;
                coordinate_c = (short) (data.method421() + last_coordinate);
                last_coordinate = coordinate_c;
                face_a[face] = coordinate_a;
                face_b[face] = coordinate_b;
                face_c[face] = coordinate_c;
            }
            if (opcode == 2) {
                coordinate_b = coordinate_c;
                coordinate_c = (short) (data.method421() + last_coordinate);
                last_coordinate = coordinate_c;
                face_a[face] = coordinate_a;
                face_b[face] = coordinate_b;
                face_c[face] = coordinate_c;
            }
            if (opcode == 3) {
                coordinate_a = coordinate_c;
                coordinate_c = (short) (data.method421() + last_coordinate);
                last_coordinate = coordinate_c;
                face_a[face] = coordinate_a;
                face_b[face] = coordinate_b;
                face_c[face] = coordinate_c;
            }
            if (opcode == 4) {
                short l14 = coordinate_a;
                coordinate_a = coordinate_b;
                coordinate_b = l14;
                coordinate_c = (short) (data.method421() + last_coordinate);
                last_coordinate = coordinate_c;
                face_a[face] = coordinate_a;
                face_b[face] = coordinate_b;
                face_c[face] = coordinate_c;
            }
        }
        data.currentOffset = mainByteBufferOffset;
        data_2.currentOffset = firstByteBufferOffset;
        data_3.currentOffset = secondByteBufferOffset;
        data_4.currentOffset = thirdByteBufferOffset;
        data_5.currentOffset = fourthByteBufferOffset;
        data_6.currentOffset = fifthByteBufferOffset;
        for (int face = 0; face < numberOfTexturedFaces; face++) {
            int i15 = texture_render_type[face] & 0xff;
            if (i15 == 0) {
                textured_face_a[face] = (short) data.readUnsignedWord();
                textured_face_b[face] = (short) data.readUnsignedWord();
                textured_face_c[face] = (short) data.readUnsignedWord();
            }
            if (i15 == 1) {
                textured_face_a[face] = (short) data_2.readUnsignedWord();
                textured_face_b[face] = (short) data_2.readUnsignedWord();
                textured_face_c[face] = (short) data_2.readUnsignedWord();
                if (modelFormat < 15) {
                    data_3.readUnsignedWord();
                    if (modelFormat < 14)
                        data_3.readUnsignedWord();
                    else
                        data_3.read3Bytes();
                    data_3.readUnsignedWord();
                } else {
                	data_3.read3Bytes();
                    data_3.read3Bytes();
                    data_3.read3Bytes();
                }
                data_4.readSignedByte();
                data_5.readSignedByte();
                data_6.readSignedByte();
            }
            if (i15 == 2) {
                textured_face_a[face] = (short) data_2.readUnsignedWord();
                textured_face_b[face] = (short) data_2.readUnsignedWord();
                textured_face_c[face] = (short) data_2.readUnsignedWord();
                if (modelFormat < 15) {
                    data_3.readUnsignedWord();
                    if (modelFormat < 14)
                        data_3.readUnsignedWord();
                    else
                        data_3.read3Bytes();
                    data_3.readUnsignedWord();
                } else {
                    data_3.read3Bytes();
                    data_3.read3Bytes();
                    data_3.read3Bytes();
                }
                data_4.readSignedByte();
                data_5.readSignedByte();
                data_6.readSignedByte();
                data_6.readSignedByte();
                data_6.readSignedByte();
            }
            if (i15 == 3) {
                textured_face_a[face] = (short) data_2.readUnsignedWord();
                textured_face_b[face] = (short) data_2.readUnsignedWord();
                textured_face_c[face] = (short) data_2.readUnsignedWord();
                if (modelFormat < 15) {
                    data_3.readUnsignedWord();
                    if (modelFormat < 14)
                    	data_3.readUnsignedWord();
                    else
                        data_3.read3Bytes();
                    data_3.readUnsignedWord();
                } else {
                    data_3.read3Bytes();
                    data_3.read3Bytes();
                    data_3.read3Bytes();
                }
                data_4.readSignedByte();
                data_5.readSignedByte();
                data_6.readSignedByte();
            }
        }
        // if (anInt364 != 255)
        // if (face_render_priorities != null)
        // for(int i12 = 0; i12 != numberOfTriangleFaces; i12++)
        // face_render_priorities[i12] = anInt364;
        data.currentOffset = k5;
        int priorityIndex;
        int triangleIndex;
        int triangleFace;
        if (second_flag) {
            triangleIndex = data.readUnsignedByte();
            for (triangleFace = 0; triangleFace != triangleIndex; triangleFace++) {
                int unknownInt = data.readUnsignedWord();
                priorityIndex = data.readUnsignedWord();
                byte face_priority;
                if (priority != -1)
                    face_priority = (byte) priority;
                else
                    face_priority = (byte) face_render_priorities[priorityIndex];
            }
            triangleFace = data.readUnsignedByte();
            if (triangleFace > 0)
                for (int unknownInt = 0; unknownInt != triangleFace; unknownInt++) {
                    priorityIndex = data.readUnsignedWord();
                    int unknownInt2 = data.readUnsignedWord();
                }

            if (has_vertex_normals) {
                triangleIndex = data.readUnsignedByte();
                if (triangleIndex > 0)
                    for (triangleFace = 0; triangleFace != triangleIndex; triangleFace++) {
                        int unknownInt = data.readUnsignedWord();
                        priorityIndex = data.readUnsignedWord();
                        int unknownInt2 = data.readUnsignedByte();
                        byte unknownByte = data.readSignedByte();
                    }
            }
        }
        downscale();
    }

    public void process600ModelData(byte modelData[]) {
        Stream data = new Stream(modelData);
        Stream data_2 = new Stream(modelData);
        Stream data_3 = new Stream(modelData);
        Stream data_4 = new Stream(modelData);
        Stream data_5 = new Stream(modelData);
        Stream data_6 = new Stream(modelData);
        Stream data_7 = new Stream(modelData);
        data.currentOffset = modelData.length - 23;
        numberOfVerticeCoordinates = data.readUnsignedWord();
        numberOfTriangleFaces = data.readUnsignedWord();
        numberOfTexturedFaces = data.readUnsignedByte();
        int flags = data.readUnsignedByte();
        boolean displayType = (flags & 0x1) != 0;
        boolean versionFormat = (flags & 0x8) != 0;
        if (!versionFormat) {
            proces525ModelData(modelData);
            return;
        }
        if (versionFormat) {
            data.currentOffset -= 7;
            modelFormat = data.readUnsignedByte();
            data.currentOffset += 6;
        }
        int priority = data.readUnsignedByte();
        int j2 = data.readUnsignedByte();
        int k2 = data.readUnsignedByte();
        int l2 = data.readUnsignedByte();
        int i3 = data.readUnsignedByte();
        int j3 = data.readUnsignedWord();
        int k3 = data.readUnsignedWord();
        int l3 = data.readUnsignedWord();
        int i4 = data.readUnsignedWord();
        int j4 = data.readUnsignedWord();
        int particle_index = 0;
        int l4 = 0;
        int i5 = 0;
        if (numberOfTexturedFaces > 0) {
            texture_render_type = new byte[numberOfTexturedFaces];
            data.currentOffset = 0;
            for (int j5 = 0; j5 < numberOfTexturedFaces; j5++) {
                byte byte0 = texture_render_type[j5] = data.readSignedByte();
                if (byte0 == 0)
                    particle_index++;
                if (byte0 >= 1 && byte0 <= 3)
                    l4++;
                if (byte0 == 2)
                    i5++;
            }
        }
        int k5 = numberOfTexturedFaces;
        int l5 = k5;
        k5 += numberOfVerticeCoordinates;
        int i6 = k5;
        if (displayType)
            k5 += numberOfTriangleFaces;
        if (flags == 1)
            k5 += numberOfTriangleFaces;
        int j6 = k5;
        k5 += numberOfTriangleFaces;
        int k6 = k5;
        if (priority == 255)
            k5 += numberOfTriangleFaces;
        int l6 = k5;
        if (k2 == 1)
            k5 += numberOfTriangleFaces;
        int i7 = k5;
        if (i3 == 1)
            k5 += numberOfVerticeCoordinates;
        int j7 = k5;
        if (j2 == 1)
            k5 += numberOfTriangleFaces;
        int k7 = k5;
        k5 += i4;
        int l7 = k5;
        if (l2 == 1)
            k5 += numberOfTriangleFaces * 2;
        int i8 = k5;
        k5 += j4;
        int j8 = k5;
        k5 += numberOfTriangleFaces * 2;
        int k8 = k5;
        k5 += j3;
        int l8 = k5;
        k5 += k3;
        int i9 = k5;
        k5 += l3;
        int j9 = k5;
        k5 += particle_index * 6;
        int k9 = k5;
        k5 += l4 * 6;
        int i_59_ = 6;
        if (modelFormat != 14) {
            if (modelFormat >= 15)
                i_59_ = 9;
        } else
            i_59_ = 7;
        int l9 = k5;
        k5 += i_59_ * l4;
        int i10 = k5;
        k5 += l4;
        int j10 = k5;
        k5 += l4;
        int k10 = k5;
        k5 += l4 + i5 * 2;
        verticesXCoordinate = new int[numberOfVerticeCoordinates];
        verticesYCoordinate = new int[numberOfVerticeCoordinates];
        verticesZCoordinate = new int[numberOfVerticeCoordinates];
        face_a = new int[numberOfTriangleFaces];
        face_b = new int[numberOfTriangleFaces];
        face_c = new int[numberOfTriangleFaces];
        if (i3 == 1)
            anIntArray1655 = new int[numberOfVerticeCoordinates];
        if (displayType)
            face_render_type = new byte[numberOfTriangleFaces];
        if (priority == 255)
            face_render_priorities = new int[numberOfTriangleFaces];
        if (j2 == 1)
            face_alpha = new byte[numberOfTriangleFaces];
        if (k2 == 1)
            anIntArray1656 = new int[numberOfTriangleFaces];
        if (l2 == 1)
            face_texture = new short[numberOfTriangleFaces];
        if (l2 == 1 && numberOfTexturedFaces > 0)
            texture_coordinates = new byte[numberOfTriangleFaces];
        face_color = new short[numberOfTriangleFaces];
        if (numberOfTexturedFaces > 0) {
            textured_face_a = new int[numberOfTexturedFaces];
            textured_face_b = new int[numberOfTexturedFaces];
            textured_face_c = new int[numberOfTexturedFaces];
        }
        data.currentOffset = l5;
        data_2.currentOffset = k8;
        data_3.currentOffset = l8;
        data_4.currentOffset = i9;
        data_5.currentOffset = i7;
        int l10 = 0;
        int i11 = 0;
        int j11 = 0;
        for (int k11 = 0; k11 < numberOfVerticeCoordinates; k11++) {
            int l11 = data.readUnsignedByte();
            int j12 = 0;
            if ((l11 & 1) != 0)
                j12 = data_2.method421();
            int l12 = 0;
            if ((l11 & 2) != 0)
                l12 = data_3.method421();
            int j13 = 0;
            if ((l11 & 4) != 0)
                j13 = data_4.method421();
            verticesXCoordinate[k11] = l10 + j12;
            verticesYCoordinate[k11] = i11 + l12;
            verticesZCoordinate[k11] = j11 + j13;
            l10 = verticesXCoordinate[k11];
            i11 = verticesYCoordinate[k11];
            j11 = verticesZCoordinate[k11];
            if (anIntArray1655 != null)
                anIntArray1655[k11] = data_5.readUnsignedByte();
        }
        data.currentOffset = j8;
        data_2.currentOffset = i6;
        data_3.currentOffset = k6;
        data_4.currentOffset = j7;
        data_5.currentOffset = l6;
        data_6.currentOffset = l7;
        data_7.currentOffset = i8;
        for (int triangle = 0; triangle < numberOfTriangleFaces; triangle++) {
            face_color[triangle] = (short) data.readUnsignedWord();
            if (flags == 1) {
                face_render_type[triangle] = data_2.readSignedByte();
            }
            if (priority == 255)
                face_render_priorities[triangle] = data_3.readSignedByte();

            if (j2 == 1) {
                face_alpha[triangle] = data_4.readSignedByte();
            }

            if (k2 == 1)
                anIntArray1656[triangle] = data_5.readUnsignedByte();

            if (l2 == 1) {
                face_texture[triangle] = (short) (data_6.readUnsignedWord() - 1);
            }
            if (texture_coordinates != null) {
                if (face_texture[triangle] == -1) {
                    texture_coordinates[triangle] = -1;
                } else {
                    texture_coordinates[triangle] = (byte) (data_7
                            .readUnsignedByte() - 1);
                }
            }
        }
        data.currentOffset = k7;
        data_2.currentOffset = j6;
        short k12 = 0;
        short i13 = 0;
        short k13 = 0;
        int l13 = 0;
        for (int i14 = 0; i14 < numberOfTriangleFaces; i14++) {
            int opcode = data_2.readUnsignedByte();
            if (opcode == 1) {
                k12 = (short) (data.method421() + l13);
                l13 = k12;
                i13 = (short) (data.method421() + l13);
                l13 = i13;
                k13 = (short) (data.method421() + l13);
                l13 = k13;
                face_a[i14] = k12;
                face_b[i14] = i13;
                face_c[i14] = k13;
            }
            if (opcode == 2) {
                i13 = k13;
                k13 = (short) (data.method421() + l13);
                l13 = k13;
                face_a[i14] = k12;
                face_b[i14] = i13;
                face_c[i14] = k13;
            }
            if (opcode == 3) {
                k12 = k13;
                k13 = (short) (data.method421() + l13);
                l13 = k13;
                face_a[i14] = k12;
                face_b[i14] = i13;
                face_c[i14] = k13;
            }
            if (opcode == 4) {
                short l14 = k12;
                k12 = i13;
                i13 = l14;
                k13 = (short) (data.method421() + l13);
                l13 = k13;
                face_a[i14] = k12;
                face_b[i14] = i13;
                face_c[i14] = k13;
            }
        }
        data.currentOffset = j9;
        data_2.currentOffset = k9;
        data_3.currentOffset = l9;
        data_4.currentOffset = i10;
        data_5.currentOffset = j10;
        data_6.currentOffset = k10;
        for (int face = 0; face < numberOfTexturedFaces; face++) {
            int i15 = texture_render_type[face] & 0xff;
            if (i15 == 0) {
                textured_face_a[face] = (short) data.readUnsignedWord();
                textured_face_b[face] = (short) data.readUnsignedWord();
                textured_face_c[face] = (short) data.readUnsignedWord();
            }
            if (i15 == 1) {
                textured_face_a[face] = (short) data_2.readUnsignedWord();
                textured_face_b[face] = (short) data_2.readUnsignedWord();
                textured_face_c[face] = (short) data_2.readUnsignedWord();
                if (modelFormat < 15) {
                    data_3.readUnsignedWord();
                    if (modelFormat >= 14)
                    	data_3.read3Bytes();
                    else
                        data_3.readUnsignedWord();
                    data_3.readUnsignedWord();
                } else {
                    data_3.read3Bytes();
                    data_3.read3Bytes();
                    data_3.read3Bytes();
                }
                data_4.readSignedByte();
                data_5.readSignedByte();
                data_6.readSignedByte();
            }
            if (i15 == 2) {
                textured_face_a[face] = (short) data_2.readUnsignedWord();
                textured_face_b[face] = (short) data_2.readUnsignedWord();
                textured_face_c[face] = (short) data_2.readUnsignedWord();
                if (modelFormat >= 15) {
                    data_3.read3Bytes();
                    data_3.read3Bytes();
                    data_3.read3Bytes();
                } else {
                    data_3.readUnsignedWord();
                    if (modelFormat < 14)
                        data_3.readUnsignedWord();
                    else
                        data_3.read3Bytes();
                    data_3.readUnsignedWord();
                }
                data_4.readSignedByte();
                data_5.readSignedByte();
                data_6.readSignedByte();
                data_6.readSignedByte();
                data_6.readSignedByte();
            }
            if (i15 == 3) {
                textured_face_a[face] = (short) data_2.readUnsignedWord();
                textured_face_b[face] = (short) data_2.readUnsignedWord();
                textured_face_c[face] = (short) data_2.readUnsignedWord();
                if (modelFormat < 15) {
                    data_3.readUnsignedWord();
                    if (modelFormat < 14)
                        data_3.readUnsignedWord();
                    else
                        data_3.read3Bytes();
                    data_3.readUnsignedWord();
                } else {
                    data_3.read3Bytes();
                    data_3.read3Bytes();
                    data_3.read3Bytes();
                }
                data_4.readSignedByte();
                data_5.readSignedByte();
                data_6.readSignedByte();
            }
        }
        downscale();
    }

    public void proces525ModelData(byte abyte0[]) {
        Stream nc1 = new Stream(abyte0);
        Stream nc2 = new Stream(abyte0);
        Stream nc3 = new Stream(abyte0);
        Stream nc4 = new Stream(abyte0);
        Stream nc5 = new Stream(abyte0);
        Stream nc6 = new Stream(abyte0);
        Stream nc7 = new Stream(abyte0);
        nc1.currentOffset = abyte0.length - 23;
        numberOfVerticeCoordinates = nc1.readUnsignedWord();
        numberOfTriangleFaces = nc1.readUnsignedWord();
        numberOfTexturedFaces = nc1.readUnsignedByte();
        int l1 = nc1.readUnsignedByte();
        boolean bool = (0x1 & l1 ^ 0xffffffff) == -2;
        int priority = nc1.readUnsignedByte();
        int j2 = nc1.readUnsignedByte();
        int k2 = nc1.readUnsignedByte();
        int l2 = nc1.readUnsignedByte();
        int i3 = nc1.readUnsignedByte();
        int j3 = nc1.readUnsignedWord();
        int k3 = nc1.readUnsignedWord();
        int l3 = nc1.readUnsignedWord();
        int i4 = nc1.readUnsignedWord();
        int j4 = nc1.readUnsignedWord();
        int particle_index = 0;
        int l4 = 0;
        int i5 = 0;
        if (numberOfTexturedFaces > 0) {
            texture_render_type = new byte[numberOfTexturedFaces];
            nc1.currentOffset = 0;
            for (int j5 = 0; j5 < numberOfTexturedFaces; j5++) {
                byte byte0 = texture_render_type[j5] = nc1.readSignedByte();
                if (byte0 == 0)
                    particle_index++;
                if (byte0 >= 1 && byte0 <= 3)
                    l4++;
                if (byte0 == 2)
                    i5++;
            }
        }
        int k5 = numberOfTexturedFaces;
        int l5 = k5;
        k5 += numberOfVerticeCoordinates;
        int i6 = k5;
        if (l1 == 1)
            k5 += numberOfTriangleFaces;
        int j6 = k5;
        k5 += numberOfTriangleFaces;
        int k6 = k5;
        if (priority == 255)
            k5 += numberOfTriangleFaces;
        int l6 = k5;
        if (k2 == 1)
            k5 += numberOfTriangleFaces;
        int i7 = k5;
        if (i3 == 1)
            k5 += numberOfVerticeCoordinates;
        int j7 = k5;
        if (j2 == 1)
            k5 += numberOfTriangleFaces;
        int k7 = k5;
        k5 += i4;
        int l7 = k5;
        if (l2 == 1)
            k5 += numberOfTriangleFaces * 2;
        int i8 = k5;
        k5 += j4;
        int j8 = k5;
        k5 += numberOfTriangleFaces * 2;
        int k8 = k5;
        k5 += j3;
        int l8 = k5;
        k5 += k3;
        int i9 = k5;
        k5 += l3;
        int j9 = k5;
        k5 += particle_index * 6;
        int k9 = k5;
        k5 += l4 * 6;
        int l9 = k5;
        k5 += l4 * 6;
        int i10 = k5;
        k5 += l4;
        int j10 = k5;
        k5 += l4;
        int k10 = k5;
        k5 += l4 + i5 * 2;
        verticesXCoordinate = new int[numberOfVerticeCoordinates];
        verticesYCoordinate = new int[numberOfVerticeCoordinates];
        verticesZCoordinate = new int[numberOfVerticeCoordinates];
        face_a = new int[numberOfTriangleFaces];
        face_b = new int[numberOfTriangleFaces];
        face_c = new int[numberOfTriangleFaces];
        if (i3 == 1)
            anIntArray1655 = new int[numberOfVerticeCoordinates];
        if (bool)
            face_render_type = new byte[numberOfTriangleFaces];
        if (priority == 255)
            face_render_priorities = new int[numberOfTriangleFaces];
        if (j2 == 1)
            face_alpha = new byte[numberOfTriangleFaces];
        if (k2 == 1)
            anIntArray1656 = new int[numberOfTriangleFaces];
        if (l2 == 1)
            face_texture = new short[numberOfTriangleFaces];
        if (l2 == 1 && numberOfTexturedFaces > 0)
            texture_coordinates = new byte[numberOfTriangleFaces];
        face_color = new short[numberOfTriangleFaces];
        if (numberOfTexturedFaces > 0) {
            textured_face_a = new int[numberOfTexturedFaces];
            textured_face_b = new int[numberOfTexturedFaces];
            textured_face_c = new int[numberOfTexturedFaces];
        }
        nc1.currentOffset = l5;
        nc2.currentOffset = k8;
        nc3.currentOffset = l8;
        nc4.currentOffset = i9;
        nc5.currentOffset = i7;
        int l10 = 0;
        int i11 = 0;
        int j11 = 0;
        for (int k11 = 0; k11 < numberOfVerticeCoordinates; k11++) {
            int l11 = nc1.readUnsignedByte();
            int j12 = 0;
            if ((l11 & 1) != 0)
                j12 = nc2.method421();
            int l12 = 0;
            if ((l11 & 2) != 0)
                l12 = nc3.method421();
            int j13 = 0;
            if ((l11 & 4) != 0)
                j13 = nc4.method421();
            verticesXCoordinate[k11] = l10 + j12;
            verticesYCoordinate[k11] = i11 + l12;
            verticesZCoordinate[k11] = j11 + j13;
            l10 = verticesXCoordinate[k11];
            i11 = verticesYCoordinate[k11];
            j11 = verticesZCoordinate[k11];
            if (anIntArray1655 != null)
                anIntArray1655[k11] = nc5.readUnsignedByte();
        }
        nc1.currentOffset = j8;
        nc2.currentOffset = i6;
        nc3.currentOffset = k6;
        nc4.currentOffset = j7;
        nc5.currentOffset = l6;
        nc6.currentOffset = l7;
        nc7.currentOffset = i8;
        for (int slot = 0; slot < numberOfTriangleFaces; slot++) {
            face_color[slot] = (short) nc1.readUnsignedWord();
            if (l1 == 1) {
                face_render_type[slot] = nc2.readSignedByte();
            }
            if (priority == 255) {
                face_render_priorities[slot] = nc3.readSignedByte();
            }
            if (j2 == 1) {
                face_alpha[slot] = nc4.readSignedByte();
            }
            if (k2 == 1)
                anIntArray1656[slot] = nc5.readUnsignedByte();

            if (l2 == 1)
                face_texture[slot] = (short) (nc6.readUnsignedWord() - 1);

            if (texture_coordinates != null)
                if (face_texture[slot] == -1)
                    texture_coordinates[slot] = -1;
                else
                    texture_coordinates[slot] = (byte) (nc7.readUnsignedByte() - 1);
        }
        nc1.currentOffset = k7;
        nc2.currentOffset = j6;
        short k12 = 0;
        short i13 = 0;
        short k13 = 0;
        int l13 = 0;
        for (int i14 = 0; i14 < numberOfTriangleFaces; i14++) {
            int opcode = nc2.readUnsignedByte();
            if (opcode == 1) {
                k12 = (short) (nc1.method421() + l13);
                l13 = k12;
                i13 = (short) (nc1.method421() + l13);
                l13 = i13;
                k13 = (short) (nc1.method421() + l13);
                l13 = k13;
                face_a[i14] = k12;
                face_b[i14] = i13;
                face_c[i14] = k13;
            }
            if (opcode == 2) {
                i13 = k13;
                k13 = (short) (nc1.method421() + l13);
                l13 = k13;
                face_a[i14] = k12;
                face_b[i14] = i13;
                face_c[i14] = k13;
            }
            if (opcode == 3) {
                k12 = k13;
                k13 = (short) (nc1.method421() + l13);
                l13 = k13;
                face_a[i14] = k12;
                face_b[i14] = i13;
                face_c[i14] = k13;
            }
            if (opcode == 4) {
                short l14 = k12;
                k12 = i13;
                i13 = l14;
                k13 = (short) (nc1.method421() + l13);
                l13 = k13;
                face_a[i14] = k12;
                face_b[i14] = i13;
                face_c[i14] = k13;
            }
        }
        nc1.currentOffset = j9;
        nc2.currentOffset = k9;
        nc3.currentOffset = l9;
        nc4.currentOffset = i10;
        nc5.currentOffset = j10;
        nc6.currentOffset = k10;
        for (int face = 0; face < numberOfTexturedFaces; face++) {
            int i15 = texture_render_type[face] & 0xff;
            if (i15 == 0) {
                textured_face_a[face] = (short) nc1.readUnsignedWord();
                textured_face_b[face] = (short) nc1.readUnsignedWord();
                textured_face_c[face] = (short) nc1.readUnsignedWord();
            }
            if (i15 == 1) {
                textured_face_a[face] = (short) nc2.readUnsignedWord();
                textured_face_b[face] = (short) nc2.readUnsignedWord();
                textured_face_c[face] = (short) nc2.readUnsignedWord();
                nc3.readUnsignedWord();
                nc3.readUnsignedWord();
                nc3.readUnsignedWord();
                nc4.readSignedByte();
                nc5.readSignedByte();
                nc6.readSignedByte();
            }
            if (i15 == 2) {
                textured_face_a[face] = (short) nc2.readUnsignedWord();
                textured_face_b[face] = (short) nc2.readUnsignedWord();
                textured_face_c[face] = (short) nc2.readUnsignedWord();
                nc3.readUnsignedWord();
                nc3.readUnsignedWord();
                nc3.readUnsignedWord();
                nc4.readSignedByte();
                nc5.readSignedByte();
                nc6.readSignedByte();
                nc6.readSignedByte();
                nc6.readSignedByte();
            }
            if (i15 == 3) {
                textured_face_a[face] = (short) nc2.readUnsignedWord();
                textured_face_b[face] = (short) nc2.readUnsignedWord();
                textured_face_c[face] = (short) nc2.readUnsignedWord();
                nc3.readUnsignedWord();
                nc3.readUnsignedWord();
                nc3.readUnsignedWord();
                nc4.readSignedByte();
                nc5.readSignedByte();
                nc6.readSignedByte();
            }
        }
    }

    public void process508ModelData(byte[] modelData) {
        boolean has_face_type = false;
        boolean has_texture_type = false;
        Stream stream = new Stream(modelData);
        Stream stream1 = new Stream(modelData);
        Stream stream2 = new Stream(modelData);
        Stream stream3 = new Stream(modelData);
        Stream stream4 = new Stream(modelData);
        stream.currentOffset = modelData.length - 18;
        numberOfVerticeCoordinates = stream.readUnsignedWord();
        numberOfTriangleFaces = stream.readUnsignedWord();
        numberOfTexturedFaces = stream.readUnsignedByte();
        int i_249_ = stream.readUnsignedByte();
        int i_250_ = stream.readUnsignedByte();
        int i_251_ = stream.readUnsignedByte();
        int i_252_ = stream.readUnsignedByte();
        int i_253_ = stream.readUnsignedByte();
        int i_254_ = stream.readUnsignedWord();
        int i_255_ = stream.readUnsignedWord();
        int i_256_ = stream.readUnsignedWord();
        int i_257_ = stream.readUnsignedWord();
        int i_258_ = 0;
        int i_259_ = i_258_;
        i_258_ += numberOfVerticeCoordinates;
        int i_260_ = i_258_;
        i_258_ += numberOfTriangleFaces;
        int i_261_ = i_258_;
        if (i_250_ == 255)
            i_258_ += numberOfTriangleFaces;
        int i_262_ = i_258_;
        if (i_252_ == 1)
            i_258_ += numberOfTriangleFaces;
        int i_263_ = i_258_;
        if (i_249_ == 1)
            i_258_ += numberOfTriangleFaces;
        int i_264_ = i_258_;
        if (i_253_ == 1)
            i_258_ += numberOfVerticeCoordinates;
        int i_265_ = i_258_;
        if (i_251_ == 1)
            i_258_ += numberOfTriangleFaces;
        int i_266_ = i_258_;
        i_258_ += i_257_;
        int i_267_ = i_258_;
        i_258_ += numberOfTriangleFaces * 2;
        int i_268_ = i_258_;
        i_258_ += numberOfTexturedFaces * 6;
        int i_269_ = i_258_;
        i_258_ += i_254_;
        int i_270_ = i_258_;
        i_258_ += i_255_;
        int i_271_ = i_258_;
        i_258_ += i_256_;
        verticesXCoordinate = new int[numberOfVerticeCoordinates];
        verticesYCoordinate = new int[numberOfVerticeCoordinates];
        verticesZCoordinate = new int[numberOfVerticeCoordinates];
        face_a = new int[numberOfTriangleFaces];
        face_b = new int[numberOfTriangleFaces];
        face_c = new int[numberOfTriangleFaces];
        if (numberOfTexturedFaces > 0) {
            texture_render_type = new byte[numberOfTexturedFaces];
            textured_face_a = new int[numberOfTexturedFaces];
            textured_face_b = new int[numberOfTexturedFaces];
            textured_face_c = new int[numberOfTexturedFaces];
        }
        if (i_253_ == 1)
            anIntArray1655 = new int[numberOfVerticeCoordinates];
        if (i_249_ == 1) {
            face_render_type = new byte[numberOfTriangleFaces];
            texture_coordinates = new byte[numberOfTriangleFaces];
            face_texture = new short[numberOfTriangleFaces];
        }
        if (i_250_ == 255)
            face_render_priorities = new int[numberOfTriangleFaces];
        if (i_251_ == 1)
            face_alpha = new byte[numberOfTriangleFaces];
        if (i_252_ == 1)
            anIntArray1656 = new int[numberOfTriangleFaces];
        face_color = new short[numberOfTriangleFaces];
        stream.currentOffset = i_259_;
        stream1.currentOffset = i_269_;
        stream2.currentOffset = i_270_;
        stream3.currentOffset = i_271_;
        stream4.currentOffset = i_264_;
        int i_272_ = 0;
        int i_273_ = 0;
        int i_274_ = 0;
        for (int i_275_ = 0; i_275_ < numberOfVerticeCoordinates; i_275_++) {
            int i_276_ = stream.readUnsignedByte();
            int i_277_ = 0;
            if ((i_276_ & 0x1) != 0)
                i_277_ = stream1.method421();
            int i_278_ = 0;
            if ((i_276_ & 0x2) != 0)
                i_278_ = stream2.method421();
            int i_279_ = 0;
            if ((i_276_ & 0x4) != 0)
                i_279_ = stream3.method421();
            verticesXCoordinate[i_275_] = i_272_ + i_277_;
            verticesYCoordinate[i_275_] = i_273_ + i_278_;
            verticesZCoordinate[i_275_] = i_274_ + i_279_;
            i_272_ = verticesXCoordinate[i_275_];
            i_273_ = verticesYCoordinate[i_275_];
            i_274_ = verticesZCoordinate[i_275_];
            if (i_253_ == 1)
                anIntArray1655[i_275_] = stream4.readUnsignedByte();
        }
        stream.currentOffset = i_267_;
        stream1.currentOffset = i_263_;
        stream2.currentOffset = i_261_;
        stream3.currentOffset = i_265_;
        stream4.currentOffset = i_262_;
        for (int i_280_ = 0; i_280_ < numberOfTriangleFaces; i_280_++) {
            face_color[i_280_] = (short) stream.readUnsignedWord();
            if (i_249_ == 1) {
                int i_281_ = stream1.readUnsignedByte();
                if ((i_281_ & 0x1) == 1) {
                    face_render_type[i_280_] = (byte) 1;
                    has_face_type = true;
                } else {
                    face_render_type[i_280_] = 0;
                }

                if ((i_281_ & 0x2) != 0) {
                    texture_coordinates[i_280_] = (byte) (i_281_ >> 2);
                    face_texture[i_280_] = face_color[i_280_];
                    face_color[i_280_] = 127;
                    // face_render_type[i_280_] = 2;
                    if (face_texture[i_280_] != -1)
                        has_texture_type = true;
                } else {
                    texture_coordinates[i_280_] = -1;
                    face_texture[i_280_] = -1;
                }
            }
            if (i_250_ == 255)
                face_render_priorities[i_280_] = stream2.readSignedByte();
            if (i_251_ == 1) {
                face_alpha[i_280_] = stream3.readSignedByte();
            }
            if (i_252_ == 1)
                anIntArray1656[i_280_] = stream4.readUnsignedByte();

        }
        stream.currentOffset = i_266_;
        stream1.currentOffset = i_260_;
        short i_282_ = 0;
        short i_283_ = 0;
        short i_284_ = 0;
        int i_285_ = 0;
        for (int face = 0; face < numberOfTriangleFaces; face++) {
            // if(face_color[face] != 65535) {
            int i_287_ = stream1.readUnsignedByte();
            if (i_287_ == 1) {
                i_282_ = (short) (stream.method421() + i_285_);
                i_285_ = i_282_;
                i_283_ = (short) (stream.method421() + i_285_);
                i_285_ = i_283_;
                i_284_ = (short) (stream.method421() + i_285_);
                i_285_ = i_284_;
                face_a[face] = i_282_;
                face_b[face] = i_283_;
                face_c[face] = i_284_;
            }
            if (i_287_ == 2) {
                i_283_ = i_284_;
                i_284_ = (short) (stream.method421() + i_285_);
                i_285_ = i_284_;
                face_a[face] = i_282_;
                face_b[face] = i_283_;
                face_c[face] = i_284_;
            }
            if (i_287_ == 3) {
                i_282_ = i_284_;
                i_284_ = (short) (stream.method421() + i_285_);
                i_285_ = i_284_;
                face_a[face] = i_282_;
                face_b[face] = i_283_;
                face_c[face] = i_284_;
            }
            if (i_287_ == 4) {
                short i_288_ = i_282_;
                i_282_ = i_283_;
                i_283_ = i_288_;
                i_284_ = (short) (stream.method421() + i_285_);
                i_285_ = i_284_;
                face_a[face] = i_282_;
                face_b[face] = i_283_;
                face_c[face] = i_284_;
            }
        }
        stream.currentOffset = i_268_;
        for (int face = 0; face < numberOfTexturedFaces; face++) {
            texture_render_type[face] = 0;
            textured_face_a[face] = (short) stream.readUnsignedWord();
            textured_face_b[face] = (short) stream.readUnsignedWord();
            textured_face_c[face] = (short) stream.readUnsignedWord();
        }
        if (texture_coordinates != null) {
            boolean textured = false;
            for (int face = 0; face < numberOfTriangleFaces; face++) {
                int coordinate = texture_coordinates[face] & 0xff;
                if (coordinate != 255) {
                    if (((textured_face_a[coordinate]) == face_a[face])
                            && ((textured_face_b[coordinate]) == face_b[face])
                            && ((textured_face_c[coordinate]) == face_c[face])) {
                        texture_coordinates[face] = -1;
                    } else {
                        textured = true;
                    }
                }
            }
            if (!textured)
                texture_coordinates = null;
        }
        if (!has_texture_type)
            face_texture = null;

        if (!has_face_type)
            face_render_type = null;

    }

    public void method464(Model model, boolean flag) {
        method464(model, flag, true, false, false);
    }

    public void method464(Model model, boolean flag, boolean texture) {
        method464(model, flag, texture, false, false);
    }

    public void method464(Model model, boolean flag, boolean texture,
                          boolean player) {
        method464(model, flag, texture, player, false);
    }

    public void method464(Model model, boolean flag, boolean texture,
                          boolean player_texture, boolean npc) {// Worn items / Npcs
        numberOfVerticeCoordinates = model.numberOfVerticeCoordinates;
        numberOfTriangleFaces = model.numberOfTriangleFaces;
        numberOfTexturedFaces = model.numberOfTexturedFaces;
        if (anIntArray1622.length < numberOfVerticeCoordinates) {
            anIntArray1622 = new int[Math.max(anIntArray1622.length * 2,
                    numberOfTriangleFaces)
                    // numberOfVerticeCoordinates + 10000
                    ];
            anIntArray1623 = new int[Math.max(anIntArray1623.length * 2,
                    numberOfTriangleFaces)
                    // numberOfVerticeCoordinates + 10000
                    ];
            anIntArray1624 = new int[Math.max(anIntArray1624.length * 2,
                    numberOfTriangleFaces)
                    // numberOfVerticeCoordinates + 10000
                    ];
        }
        verticesXCoordinate = anIntArray1622;
        verticesYCoordinate = anIntArray1623;
        verticesZCoordinate = anIntArray1624;
        for (int k = 0; k < numberOfVerticeCoordinates; k++) {
            if (model.verticesXCoordinate != null)
                verticesXCoordinate[k] = model.verticesXCoordinate[k];
            if (model.verticesYCoordinate != null)
                verticesYCoordinate[k] = model.verticesYCoordinate[k];
            if (model.verticesZCoordinate != null)
                verticesZCoordinate[k] = model.verticesZCoordinate[k];
        }
        if (numberOfTexturedFaces > 0) {
            textured_face_a = model.textured_face_a;
            textured_face_b = model.textured_face_b;
            textured_face_c = model.textured_face_c;

            if (texture) {
                face_texture = model.face_texture;
            } else {
                face_texture = new short[numberOfTriangleFaces];
                if (model.face_texture != null)
                    for (int l = 0; l < numberOfTriangleFaces; l++)
                        face_texture[l] = -1;
            }
            force_texture = npc;// npc;
            display_model_specific_texture = texture;
            texture_coordinates = model.texture_coordinates;
            texture_render_type = model.texture_render_type;

        }
        if (flag) {
            face_alpha = model.face_alpha;
        } else {
            if (anIntArray1625.length < numberOfTriangleFaces)
                anIntArray1625 = new byte[Math.max(anIntArray1625.length * 2,
                        numberOfTriangleFaces)
                        // numberOfTriangleFaces + 100
                        ];
            face_alpha = anIntArray1625;
            if (model.face_alpha == null) {
                for (int l = 0; l < numberOfTriangleFaces; l++)
                    face_alpha[l] = 0;

            } else {
                for (int i1 = 0; i1 != numberOfTriangleFaces; i1++)
                    face_alpha[i1] = model.face_alpha[i1];

            }
        }
        face_render_type = model.face_render_type;
        if (flag) {
            face_color = model.face_color;
        } else {
            if (anIntArray1626.length < numberOfTriangleFaces)
                anIntArray1626 = new short[Math.max(anIntArray1626.length * 2,
                        numberOfTriangleFaces)];

            face_color = anIntArray1626;
            if (model.face_color != null)
                for (int l = 0; l < numberOfTriangleFaces; l++)
                    face_color[l] = model.face_color[l];
        }
        face_render_priorities = model.face_render_priorities;
        face_priority = model.face_priority;
        triangleSkin = model.triangleSkin;
        vertexSkin = model.vertexSkin;
        face_a = model.face_a;
        face_b = model.face_b;
        face_c = model.face_c;
        face_shade_a = model.face_shade_a;
        face_shade_b = model.face_shade_b;
        face_shade_c = model.face_shade_c;
    }

	private final int method465(Model model, int i) {
		int k = model.verticesXCoordinate[i];
		int l = model.verticesYCoordinate[i];
		int i1 = model.verticesZCoordinate[i];
		for (int j1 = 0; j1 < numberOfVerticeCoordinates; j1++) {
			if (k == verticesXCoordinate[j1] && l == verticesYCoordinate[j1] && i1 == verticesZCoordinate[j1]) {
				return j1;
			}
		}

		verticesXCoordinate[numberOfVerticeCoordinates] = k;
		verticesYCoordinate[numberOfVerticeCoordinates] = l;
		verticesZCoordinate[numberOfVerticeCoordinates] = i1;
		if (model.anIntArray1655 != null)
			anIntArray1655[numberOfVerticeCoordinates] = model.anIntArray1655[i];
		return numberOfVerticeCoordinates++;
	}

	public void calculateDiagonals() {// calculateDiagonals
		if (boundsType != 1) {
			boundsType = 1;
			highestY = 0;
			lowestY = 0;
			lowestX = 0;
			for (int i = 0; i < numberOfVerticeCoordinates; i++) {
				int j = verticesXCoordinate[i];// verticesXCoordinate
				int k = verticesYCoordinate[i];// verticesYCoordinate
				int l = verticesZCoordinate[i];// verticesZCoordinate
				if (-k > highestY)
					highestY = -k;
				if (k > lowestY)
					lowestY = k;
				int i1 = j * j + l * l;
				if (i1 > lowestX)
					lowestX = i1;
			}
			lowestX = (int) (Math.sqrt(lowestX) + 0.99);
			highestZ = (int) (Math.sqrt(lowestX * lowestX + highestY * highestY) + 0.99);
			highestX = highestZ + (int) (Math.sqrt(lowestX * lowestX + lowestY * lowestY) + 0.99);
		}
	}

	public void normalise() {
		if (boundsType != 2) {
			highestY = 0;
			lowestX = 0;
			for (int i = 0; i < numberOfVerticeCoordinates; i++) {
				int j = verticesXCoordinate[i];
				int k = verticesYCoordinate[i];
				int l = verticesZCoordinate[i];
				int i1 = j * j + l * l + k * k;
				if (i1 > lowestX)
					lowestX = i1;
			}
			lowestX = (int) (Math.sqrt((double) lowestX) + 0.99);
			highestZ = lowestX;
			highestX = lowestX + lowestX;
		}
	}

	public void method468() {
		if (boundsType != 3) {
			boundsType = 3;
			highestY = 0;
			lowestY = 0;
			lowestX = 999999;
			highestX = -999999;
			highestZ = -999999;
			lowestZ = 999999;
			for (int j = 0; j < numberOfVerticeCoordinates; j++) {
				int xVertex = verticesXCoordinate[j];
				int yVertex = verticesYCoordinate[j];
				int zVertex = verticesZCoordinate[j];
				if (xVertex < lowestX)
					lowestX = xVertex;
				if (xVertex > highestX)
					highestX = xVertex;
				if (zVertex < lowestZ)
					lowestZ = zVertex;
				if (zVertex > highestZ)
					highestZ = zVertex;
				if (-yVertex > highestY)
					highestY = -yVertex;
				if (yVertex > lowestY)
					lowestY = yVertex;
			}
		}
	}

    public void createBones() {
        if (anIntArray1655 != null) {
            int ai[] = new int[256];
            int j = 0;
            for (int l = 0; l < numberOfVerticeCoordinates; l++) {
                int j1 = anIntArray1655[l];
                ai[j1]++;
                if (j1 > j)
                    j = j1;
            }

            vertexSkin = new int[j + 1][];
            for (int k1 = 0; k1 <= j; k1++) {
                vertexSkin[k1] = new int[ai[k1]];
                ai[k1] = 0;
            }

            for (int j2 = 0; j2 < numberOfVerticeCoordinates; j2++) {
                int l2 = anIntArray1655[j2];
                vertexSkin[l2][ai[l2]++] = j2;
            }

            anIntArray1655 = null;
        }
        if (anIntArray1656 != null) {
            int ai[] = new int[256];
            int k = 0;
            for (int i1 = 0; i1 < numberOfTriangleFaces; i1++) {
                int l1 = anIntArray1656[i1];
                ai[l1]++;
                if (l1 > k)
                    k = l1;
            }

            triangleSkin = new int[k + 1][];
            for (int i2 = 0; i2 <= k; i2++) {
                triangleSkin[i2] = new int[ai[i2]];
                ai[i2] = 0;
            }

            for (int k2 = 0; k2 < numberOfTriangleFaces; k2++) {
                int i3 = anIntArray1656[k2];
                triangleSkin[i3][ai[i3]++] = k2;
            }

            anIntArray1656 = null;
        }
    }

    public void applyTransform(int frame) {
        if (vertexSkin == null)
            return;
        if (frame == -1)
            return;
        FrameReader class36 = FrameReader.forId(frame);
        if (class36 == null)
            return;
        SkinList class18 = class36.skinList;
        anInt1681 = 0;
        anInt1682 = 0;
        anInt1683 = 0;
        for (int k = 0; k < class36.stepCount; k++) {
            int l = class36.opcodeLinkTable[k];
            method472(class18.opcode[l], class18.skinList[l],
                    class36.modifier1[k], class36.modifier2[k],
                    class36.modifier3[k]);
        }
        endAnimation();
    }

    public void interpolateFrames(int frame, int nextFrame, int cycle, int length, boolean[] animateLabels, boolean condition) {
		if (vertexSkin == null) {
			return;
		}
		final FrameReader currAnim = FrameReader.forId(frame);
		if (currAnim == null) {
			return;
		}
		final SkinList skinList = currAnim.skinList;
		FrameReader nextAnim = null;
    	if (nextFrame != -1) {
    		nextAnim = FrameReader.forId(nextFrame);
    		if (nextAnim != null && nextAnim.skinList != skinList) {
    			nextAnim = null;
    		}
    	}
    	anInt1681 = 0;
    	anInt1682 = 0;
    	anInt1683 = 0;
		if (nextAnim == null || cycle == 0) {
			for (int index = 0; index < currAnim.stepCount; index++) {
				int anim = currAnim.opcodeLinkTable[index];
				if (animateLabels == null || animateLabels[anim] == condition || skinList.opcode[anim] == 0) {
					method472(skinList.opcode[anim], skinList.skinList[anim], currAnim.modifier1[index], currAnim.modifier2[index], currAnim.modifier3[index]);
				}
			}
		} else {
	    	int currFrameId = 0;
	    	int nextFrameId = 0;
		    for (int index = 0; index < skinList.length; index++) {
  		    	boolean interpolateCurr = false;
		    	if (currFrameId < currAnim.stepCount && currAnim.opcodeLinkTable[currFrameId] == index) {
		    		interpolateCurr = true;
		    	}
		    	boolean interpolateNext = false;
		    	if (nextFrameId < nextAnim.stepCount && nextAnim.opcodeLinkTable[nextFrameId] == index) {
		    		interpolateNext = true;
		    	}
		    	if (interpolateCurr || interpolateNext) {
		    		if (animateLabels != null && animateLabels[index] != condition && skinList.opcode[index] != 0) {
		    			if (interpolateCurr) {
		    				currFrameId++;
		    			}
		    			if (interpolateNext) {
		    				nextFrameId++;
		    			}
		    		} else {
						int defaultModifier = 0;
						int opcode = skinList.opcode[index];
						if (opcode == 3) {
							defaultModifier = 128;
						}
						int currAnimX;
						int currAnimY;
						int currAnimZ;
						byte currFlag;
						if (interpolateCurr) {
							currAnimX = currAnim.modifier1[currFrameId];
							currAnimY = currAnim.modifier2[currFrameId];
							currAnimZ = currAnim.modifier3[currFrameId];
							currFlag = currAnim.modifier4[currFrameId];
							currFrameId++;
						} else {
							currAnimX = defaultModifier;
							currAnimY = defaultModifier;
							currAnimZ = defaultModifier;
							currFlag = 0;
						}
						int nextAnimX;
						int nextAnimY;
						int nextAnimZ;
						byte nextFlag;
						if (interpolateNext) {
							nextAnimX = nextAnim.modifier1[nextFrameId];
							nextAnimY = nextAnim.modifier2[nextFrameId];
							nextAnimZ = nextAnim.modifier3[nextFrameId];
							nextFlag = nextAnim.modifier4[nextFrameId];
							nextFrameId++;
						} else {
							nextAnimX = defaultModifier;
							nextAnimY = defaultModifier;
							nextAnimZ = defaultModifier;
							nextFlag = 0;
						}
						int interpolatedX;
						int interpolatedY;
						int interpolatedZ;
						if ((nextFlag & 0x2) != 0 || (currFlag & 0x1) != 0) {
							interpolatedX = currAnimX;
							interpolatedY = currAnimY;
							interpolatedZ = currAnimZ;
						} else if (opcode == 2) {
							int deltaX = nextAnimX - currAnimX & 0x3fff;
							int deltaY = nextAnimY - currAnimY & 0x3fff;
							int deltaZ = nextAnimZ - currAnimZ & 0x3fff;
							if (deltaX >= 8192) {
								deltaX -= 16384;
							}
							if (deltaY >= 8192) {
								deltaY -= 16384;
							}
							if (deltaZ >= 8192) {
								deltaZ -= 16384;
							}
							interpolatedX = currAnimX + deltaX * cycle / length & 0x3fff;
							interpolatedY = currAnimY + deltaY * cycle / length & 0x3fff;
							interpolatedZ = currAnimZ + deltaZ * cycle / length & 0x3fff;
						} else {
							interpolatedX = currAnimX + (nextAnimX - currAnimX) * cycle / length;
							interpolatedY = currAnimY + (nextAnimY - currAnimY) * cycle / length;
							interpolatedZ = currAnimZ + (nextAnimZ - currAnimZ) * cycle / length;
						}
						method472(opcode, skinList.skinList[index], interpolatedX, interpolatedY, interpolatedZ);
		    		}
		    	}
		    }
	    }
		endAnimation();
    }

    public void interpolateFrames(SkinList skinList, FrameReader currAnim, FrameReader nextAnim, int cycle, int length, boolean[] animateLabels, boolean condition) {
		if (nextAnim == null || cycle == 0) {
			for (int index = 0; index < currAnim.stepCount; index++) {
				int anim = currAnim.opcodeLinkTable[index];
				if (animateLabels == null || animateLabels[anim] == condition || skinList.opcode[anim] == 0) {
					method472(skinList.opcode[anim], skinList.skinList[anim], currAnim.modifier1[index], currAnim.modifier2[index], currAnim.modifier3[index]);
				}
			}
		} else {
	    	int currFrameId = 0;
	    	int nextFrameId = 0;
		    for (int index = 0; index < skinList.length; index++) {
  		    	boolean interpolateCurr = false;
		    	if (currFrameId < currAnim.stepCount && currAnim.opcodeLinkTable[currFrameId] == index) {
		    		interpolateCurr = true;
		    	}
		    	boolean interpolateNext = false;
		    	if (nextFrameId < nextAnim.stepCount && nextAnim.opcodeLinkTable[nextFrameId] == index) {
		    		interpolateNext = true;
		    	}
		    	if (interpolateCurr || interpolateNext) {
		    		if (animateLabels != null && animateLabels[index] != condition && skinList.opcode[index] != 0) {
		    			if (interpolateCurr) {
		    				currFrameId++;
		    			}
		    			if (interpolateNext) {
		    				nextFrameId++;
		    			}
		    		} else {
						int defaultModifier = 0;
						int opcode = skinList.opcode[index];
						if (opcode == 3) {
							defaultModifier = 128;
						}
						int currAnimX;
						int currAnimY;
						int currAnimZ;
						byte currFlag;
						if (interpolateCurr) {
							currAnimX = currAnim.modifier1[currFrameId];
							currAnimY = currAnim.modifier2[currFrameId];
							currAnimZ = currAnim.modifier3[currFrameId];
							currFlag = currAnim.modifier4[currFrameId];
							currFrameId++;
						} else {
							currAnimX = defaultModifier;
							currAnimY = defaultModifier;
							currAnimZ = defaultModifier;
							currFlag = 0;
						}
						int nextAnimX;
						int nextAnimY;
						int nextAnimZ;
						byte nextFlag;
						if (interpolateNext) {
							nextAnimX = nextAnim.modifier1[nextFrameId];
							nextAnimY = nextAnim.modifier2[nextFrameId];
							nextAnimZ = nextAnim.modifier3[nextFrameId];
							nextFlag = nextAnim.modifier4[nextFrameId];
							nextFrameId++;
						} else {
							nextAnimX = defaultModifier;
							nextAnimY = defaultModifier;
							nextAnimZ = defaultModifier;
							nextFlag = 0;
						}
						int interpolatedX;
						int interpolatedY;
						int interpolatedZ;
						if ((nextFlag & 0x2) != 0 || (currFlag & 0x1) != 0) {
							interpolatedX = currAnimX;
							interpolatedY = currAnimY;
							interpolatedZ = currAnimZ;
						} else if (opcode == 2) {
							int deltaX = nextAnimX - currAnimX & 0x3fff;
							int deltaY = nextAnimY - currAnimY & 0x3fff;
							int deltaZ = nextAnimZ - currAnimZ & 0x3fff;
							if (deltaX >= 8192) {
								deltaX -= 16384;
							}
							if (deltaY >= 8192) {
								deltaY -= 16384;
							}
							if (deltaZ >= 8192) {
								deltaZ -= 16384;
							}
							interpolatedX = currAnimX + deltaX * cycle / length & 0x3fff;
							interpolatedY = currAnimY + deltaY * cycle / length & 0x3fff;
							interpolatedZ = currAnimZ + deltaZ * cycle / length & 0x3fff;
						} else {
							interpolatedX = currAnimX + (nextAnimX - currAnimX) * cycle / length;
							interpolatedY = currAnimY + (nextAnimY - currAnimY) * cycle / length;
							interpolatedZ = currAnimZ + (nextAnimZ - currAnimZ) * cycle / length;
						}
						method472(opcode, skinList.skinList[index], interpolatedX, interpolatedY, interpolatedZ);
		    		}
		    	}
		    }
	    }
		endAnimation();
    }

    /*public void method471(boolean ai[], int idleAnimFrame, int idleAnimNextFrame, int idleAnimDelay, int idleAnimFrameDelay, int currentFrame, int nextFrame, int currentDelay, int currrentFrameDelay) {
        if (currentFrame != -1) {
	    	if (ai == null || idleAnimFrame == -1) {
	    		interpolateFrames(currentFrame, nextFrame, currentDelay, currrentFrameDelay, null, false);
	        } else if (vertexSkin != null) {
	        	//for (int i = 0; i < ai.length; i++) {
	        	//	if(ai[i]) {
	        	//		System.out.print(i + ", ");
	        	//	}
	        	//}
				anInt1681 = 0;
				anInt1682 = 0;
				anInt1683 = 0;
				FrameReader animFrame = FrameReader.forId(currentFrame);
				SkinList animFrameBase = animFrame.skinList;
				FrameReader animNextFrame = null;
				if (nextFrame != -1) {
					animNextFrame = FrameReader.forId(nextFrame);
					if (animNextFrame != null && animNextFrame.skinList != animFrameBase) {
						animNextFrame = null;
					}
				}
				FrameReader idleFrame = FrameReader.forId(idleAnimFrame);
				FrameReader idleNextFrame = null;
				if (idleAnimNextFrame != -1) {
					idleNextFrame = FrameReader.forId(idleAnimNextFrame);
					if (idleNextFrame != null && idleNextFrame.skinList != animFrameBase) {
						idleNextFrame = null;
					}
				}
	        	interpolateFrames(animFrameBase, animFrame, animNextFrame, currentDelay, currrentFrameDelay, ai, false);
	        	method472(0, new int[0], 0, 0, 0);
	        	interpolateFrames(animFrameBase, idleFrame, idleNextFrame, idleAnimDelay, idleAnimFrameDelay, ai, true);
	        }
        }
    }*/
    
    public void method471(boolean ai[], int idleAnimFrame, int idleAnimNextFrame, int idleAnimDelay, int idleAnimFrameDelay, int currentFrame, int nextFrame, int currentDelay, int currentFrameDelay) {
        if (currentFrame == -1 || vertexSkin == null)
            return;
        if (ai == null || idleAnimFrame == -1) {
            applyTransform(currentFrame);
            return;
        }
        /*for (int i = 0; i < ai.length; i++) {
        	if(ai[i]) {
        		System.out.print(i + ", ");
        	}
        }*/
        interpolateFrames(currentFrame, nextFrame, currentDelay, currentFrameDelay, ai, false);
        method472(0, new int[0], 0, 0, 0);
        interpolateFrames(idleAnimFrame, idleAnimNextFrame, idleAnimDelay, idleAnimFrameDelay, ai, true);
        endAnimation();
    }

	private void endAnimation() {
		if (scaledVertices) {
			for (int index = 0; index < numberOfVerticeCoordinates; index++) {
				verticesXCoordinate[index] = verticesXCoordinate[index] + 7 >> 4;
				verticesYCoordinate[index] = verticesYCoordinate[index] + 7 >> 4;
				verticesZCoordinate[index] = verticesZCoordinate[index] + 7 >> 4;
			}
			scaledVertices = false;
		}

    	vertexNormals = null;
    	boundsType = 0;
	}

	private void method472(int i, int ai[], int j, int k, int l) {
		int i1 = ai.length;
		if (i == 0) {
			j <<= 4;
			k <<= 4;
			l <<= 4;
			if (!scaledVertices) {
				for (int i_275_ = 0; i_275_ < numberOfVerticeCoordinates; i_275_++) {
					verticesXCoordinate[i_275_] <<= 4;
					verticesYCoordinate[i_275_] <<= 4;
					verticesZCoordinate[i_275_] <<= 4;
				}
				scaledVertices = true;
			}

			int j1 = 0;
			anInt1681 = 0;
			anInt1682 = 0;
			anInt1683 = 0;
			for (int k2 = 0; k2 < i1; k2++) {
				int l3 = ai[k2];
				if (l3 < vertexSkin.length) {
					int ai5[] = vertexSkin[l3];
					for (int i5 = 0; i5 < ai5.length; i5++) {
						int j6 = ai5[i5];
						anInt1681 += verticesXCoordinate[j6];
						anInt1682 += verticesYCoordinate[j6];
						anInt1683 += verticesZCoordinate[j6];
						j1++;
					}

				}
			}

			if (j1 > 0) {
				anInt1681 = anInt1681 / j1 + j;
				anInt1682 = anInt1682 / j1 + k;
				anInt1683 = anInt1683 / j1 + l;
				return;
			} else {
				anInt1681 = j;
				anInt1682 = k;
				anInt1683 = l;
				return;
			}
		}
		if (i == 1) {
			j <<= 4;
			k <<= 4;
			l <<= 4;
			if (!scaledVertices) {
				for (int i_275_ = 0; i_275_ < numberOfVerticeCoordinates; i_275_++) {
					verticesXCoordinate[i_275_] <<= 4;
					verticesYCoordinate[i_275_] <<= 4;
					verticesZCoordinate[i_275_] <<= 4;
				}
				scaledVertices = true;
			}

			for (int k1 = 0; k1 < i1; k1++) {
				int l2 = ai[k1];
				if (l2 < vertexSkin.length) {
					int ai1[] = vertexSkin[l2];
					for (int i4 = 0; i4 < ai1.length; i4++) {
						int j5 = ai1[i4];
						verticesXCoordinate[j5] += j;
						verticesYCoordinate[j5] += k;
						verticesZCoordinate[j5] += l;
					}

				}
			}

			return;
		}
		if (i == 2) {
			for (int l1 = 0; l1 < i1; l1++) {
				int i3 = ai[l1];
				if (i3 < vertexSkin.length) {
					int ai2[] = vertexSkin[i3];
					for (int j4 = 0; j4 < ai2.length; j4++) {
						int k5 = ai2[j4];
						verticesXCoordinate[k5] -= anInt1681;
						verticesYCoordinate[k5] -= anInt1682;
						verticesZCoordinate[k5] -= anInt1683;
						if (l != 0) {
							int j7 = Rasterizer.SINE[l];
							int i8 = Rasterizer.COSINE[l];
							int l8 = (verticesYCoordinate[k5] * j7 + verticesXCoordinate[k5] * i8 + 32767) >> 15;
							verticesYCoordinate[k5] = (verticesYCoordinate[k5] * i8 - verticesXCoordinate[k5] * j7 + 32767) >> 15;
							verticesXCoordinate[k5] = l8;
						}
						if (j != 0) {
							int k7 = Rasterizer.SINE[j];
							int j8 = Rasterizer.COSINE[j];
							int i9 = (verticesYCoordinate[k5] * j8 - verticesZCoordinate[k5] * k7 + 32767) >> 15;
							verticesZCoordinate[k5] = (verticesYCoordinate[k5] * k7 + verticesZCoordinate[k5] * j8 + 32767) >> 15;
							verticesYCoordinate[k5] = i9;
						}
						if (k != 0) {
							int l7 = Rasterizer.SINE[k];
							int k8 = Rasterizer.COSINE[k];
							int j9 = (verticesZCoordinate[k5] * l7 + verticesXCoordinate[k5] * k8 + 32767) >> 15;
							verticesZCoordinate[k5] = (verticesZCoordinate[k5] * k8 - verticesXCoordinate[k5] * l7 + 32767) >> 15;
							verticesXCoordinate[k5] = j9;
						}
						verticesXCoordinate[k5] += anInt1681;
						verticesYCoordinate[k5] += anInt1682;
						verticesZCoordinate[k5] += anInt1683;
					}

				}
			}
			return;
		}
		if (i == 3) {
			for (int i2 = 0; i2 < i1; i2++) {
				int j3 = ai[i2];
				if (j3 < vertexSkin.length) {
					int ai3[] = vertexSkin[j3];
					for (int k4 = 0; k4 < ai3.length; k4++) {
						int l5 = ai3[k4];
						verticesXCoordinate[l5] -= anInt1681;
						verticesYCoordinate[l5] -= anInt1682;
						verticesZCoordinate[l5] -= anInt1683;
						verticesXCoordinate[l5] = (verticesXCoordinate[l5] * j) / 128;
						verticesYCoordinate[l5] = (verticesYCoordinate[l5] * k) / 128;
						verticesZCoordinate[l5] = (verticesZCoordinate[l5] * l) / 128;
						verticesXCoordinate[l5] += anInt1681;
						verticesYCoordinate[l5] += anInt1682;
						verticesZCoordinate[l5] += anInt1683;
					}
				}
			}
			return;
		}
		if (i == 5 && triangleSkin != null && face_alpha != null) {
			for (int j2 = 0; j2 < i1; j2++) {
				int k3 = ai[j2];
				if (k3 < triangleSkin.length) {
					int ai4[] = triangleSkin[k3];
					for (int l4 = 0; l4 < ai4.length; l4++) {
						int i6 = ai4[l4];
						face_alpha[i6] += j * 8;
						if (face_alpha[i6] < 0)
							face_alpha[i6] = 0;
						else if (face_alpha[i6] > 255)
							face_alpha[i6] = (byte) 255;
					}
				}
			}
		}
	}

	public void rotateBy270() {
		vertexNormals = null;
		boundsType = 0;
		for (int j = 0; j < numberOfVerticeCoordinates; j++) {
			int k = verticesZCoordinate[j];
			verticesZCoordinate[j] = verticesXCoordinate[j];
			verticesXCoordinate[j] = -k;
		}
	}

	public void rotateBy180() {
		vertexNormals = null;
		boundsType = 0;
		for (int j = 0; j < numberOfVerticeCoordinates; j++) {
			verticesXCoordinate[j] = -verticesXCoordinate[j];
			verticesZCoordinate[j] = -verticesZCoordinate[j];
		}
	}

	public void rotateBy90() {
		vertexNormals = null;
		boundsType = 0;
		for (int j = 0; j < numberOfVerticeCoordinates; j++) {
			int k = verticesXCoordinate[j];
			verticesXCoordinate[j] = verticesZCoordinate[j];
			verticesZCoordinate[j] = -k;
		}
	}

    public void method474(int i) {
    	vertexNormals = null;
    	boundsType = 0;
        int k = Rasterizer.SINE[i];
        int l = Rasterizer.COSINE[i];
        for (int i1 = 0; i1 < numberOfVerticeCoordinates; i1++) {
            int j1 = verticesYCoordinate[i1] * l - verticesZCoordinate[i1] * k >> 15;
            verticesZCoordinate[i1] = verticesYCoordinate[i1] * k
                    + verticesZCoordinate[i1] * l >> 15;
            verticesYCoordinate[i1] = j1;
        }
    }

    public void translate(int x, int y, int z) {
     	vertexNormals = null;
    	boundsType = 0;
        for (int i1 = 0; i1 < numberOfVerticeCoordinates; i1++) {
            verticesXCoordinate[i1] += x;
            verticesYCoordinate[i1] += y;
            verticesZCoordinate[i1] += z;
        }
    }

    public void setColor(int oldColor, int newColor) {
        if (face_color != null) {
            for (int k = 0; k < numberOfTriangleFaces; k++) {
                if (face_color[k] == (short) oldColor) {
                    face_color[k] = (short) newColor;
                }
            }
        }
    }

    public void setColor(short oldColor, short newColor) {
        if (face_color != null) {
            for (int k = 0; k < numberOfTriangleFaces; k++) {
                if (face_color[k] == oldColor) {
                    face_color[k] = newColor;
                }
            }
        }
    }

    public void writeAllColor() {
        try {
            PrintWriter pw = new PrintWriter("vinecolors.txt");
            if (face_color != null)
                for (int k = 0; k < numberOfTriangleFaces; k++)
                    pw.println(face_color[k]);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeAllTextures() {
        try {
            PrintWriter pw = new PrintWriter("vinetextures.txt");
            if (face_texture != null)
                for (int k = 0; k < numberOfTriangleFaces; k++)
                    pw.println(face_texture[k]);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recolorAll(int newColor) {
        if (face_color != null)
            for (int k = 0; k < numberOfTriangleFaces; k++)
                face_color[k] = (short) newColor;
    }

    public void recolorAllTextures(int newColor) {
        if (face_texture != null)
            for (int k = 0; k < numberOfTriangleFaces; k++)
                face_texture[k] = (short) newColor;
    }

    public void setTexture(short i, short j) {
        if (face_texture != null)
            for (int k = 0; k < numberOfTriangleFaces; k++)
                if (face_texture[k] == i)
                    face_texture[k] = j;

    }

    private void downscale() {
    	vertexNormals = null;
    	boundsType = 0;
        for (int i = 0; i != numberOfVerticeCoordinates; ++i) {
            verticesXCoordinate[i] = (verticesXCoordinate[i] + 7) >> 2;
            verticesYCoordinate[i] = (verticesYCoordinate[i] + 7) >> 2;
            verticesZCoordinate[i] = (verticesZCoordinate[i] + 7) >> 2;
        }
    }

    public void mirrorModel() {
    	vertexNormals = null;
    	boundsType = 0;
        for (int j = 0; j < numberOfVerticeCoordinates; j++)
            verticesZCoordinate[j] = -verticesZCoordinate[j];
        int temp;
        for (int k = 0; k < numberOfTriangleFaces; k++) {
            temp = face_a[k];
            face_a[k] = face_c[k];
            face_c[k] = temp;
        }
    }

    public void scale(int X, int Y, int Z) {
    	vertexNormals = null;
    	boundsType = 0;
        for (int i1 = 0; i1 < numberOfVerticeCoordinates; i1++) {
            verticesXCoordinate[i1] = (verticesXCoordinate[i1] * X) / 128;
            verticesYCoordinate[i1] = (verticesYCoordinate[i1] * Y) / 128;
            verticesZCoordinate[i1] = (verticesZCoordinate[i1] * Z) / 128;
        }

    }

    /**
     * Since this method for lighting is called for most of the different ways
     * that the models are catagorized(entity, objects, items) you can set a
     * specific flag (disable object/entity textures, but leave item, and others
     * enabled)
     * <p/>
     * <p/>
     * !Graphics do not seem to follow the same form of rendering! (They apply
     * in the model connect method)
     *
     * @param int       i - moderate_light_level
     * @param int       j - magnitude_direction
     * @param int       k - light_fall_x
     * @param int       l - light_fall_y
     * @param int       i1 - light_fall_z
     * @param boolean   flag - flat_shading true - Flat shading false - Gouraud
     *                  shading (smooth)
     * @param boolean   fix_priorities true - fixes model priorities false - keeps
     *                  model specific priorities
     * @param texture   true - render specific model textures false - disable specific
     *                  model textures
     * @param color_fix true - remove specific triangle colors per model (applying new
     *                  alpha values) false - render specific triangle colors per
     *                  model (keeps specific alpha values)
     */
    public final void setLighting(int i, int j, int k, int l, int i1, boolean flag) {
        setLighting(i, j, k, l, i1, flag, false, true);
    }

    public final void setLighting(int i, int j, int k, int l, int i1,
                                  boolean flag, boolean texture) {
        setLighting(i, j, k, l, i1, flag, texture, false);
    }

    public final void setLighting(int i, int j, int k, int l, int i1,  boolean flag, boolean texture, boolean color_fix) {
    	display_model_specific_texture = texture;
        int j1 = (int) Math.sqrt(k * k + l * l + i1 * i1);
        int k1 = j * j1 >> 8;
        if (face_shade_a == null) {
            face_shade_a = new int[numberOfTriangleFaces];
            face_shade_b = new int[numberOfTriangleFaces];
            face_shade_c = new int[numberOfTriangleFaces];
        }
        if (vertexNormals == null) {
            vertexNormals = new VertexNormal[numberOfVerticeCoordinates];
            for (int l1 = 0; l1 < numberOfVerticeCoordinates; l1++)
                vertexNormals[l1] = new VertexNormal();
        }
        for (int i2 = 0; i2 < numberOfTriangleFaces; i2++) {
            int j2 = face_a[i2] & 0xffff;
            int l2 = face_b[i2] & 0xffff;
            int i3 = face_c[i2] & 0xffff;
            int j3 = verticesXCoordinate[l2] - verticesXCoordinate[j2];
            int k3 = verticesYCoordinate[l2] - verticesYCoordinate[j2];
            int l3 = verticesZCoordinate[l2] - verticesZCoordinate[j2];
            int i4 = verticesXCoordinate[i3] - verticesXCoordinate[j2];
            int j4 = verticesYCoordinate[i3] - verticesYCoordinate[j2];
            int k4 = verticesZCoordinate[i3] - verticesZCoordinate[j2];
            int l4 = k3 * k4 - j4 * l3;
            int i5 = l3 * i4 - k4 * j3;
            int j5;
            for (j5 = j3 * j4 - i4 * k3; l4 > 8192 || i5 > 8192 || j5 > 8192
                    || l4 < -8192 || i5 < -8192 || j5 < -8192; j5 >>= 1) {
                l4 >>= 1;
                i5 >>= 1;
            }

            int k5 = (int) Math.sqrt(l4 * l4 + i5 * i5 + j5 * j5);
            if (k5 <= 0)
                k5 = 1;
            l4 = (l4 * 256) / k5;
            i5 = (i5 * 256) / k5;
            j5 = (j5 * 256) / k5;

            if (face_render_type == null || (face_render_type[i2] & 1) == 0) {
                VertexNormal class33_2 = vertexNormals[j2];
                class33_2.x += l4;
                class33_2.y += i5;
                class33_2.z += j5;
                class33_2.magnitude++;
                class33_2 = vertexNormals[l2];
                class33_2.x += l4;
                class33_2.y += i5;
                class33_2.z += j5;
                class33_2.magnitude++;
                class33_2 = vertexNormals[i3];
                class33_2.x += l4;
                class33_2.y += i5;
                class33_2.z += j5;
                class33_2.magnitude++;
            } else {
                int l5 = i + (k * l4 + l * i5 + i1 * j5) / (k1 + k1 / 2);
                face_shade_a[i2] = method481(face_color[i2] & 0xffff, l5,
                        face_render_type[i2]);

            }
        }

        if (flag) {
            method480(i, k1, k, l, i1);
        } else {
            vertexNormalOffset = new VertexNormal[numberOfVerticeCoordinates];
            for (int k2 = 0; k2 < numberOfVerticeCoordinates; k2++) {
                VertexNormal class33 = vertexNormals[k2];
                VertexNormal class33_1 = vertexNormalOffset[k2] = new VertexNormal();
                class33_1.x = class33.x;
                class33_1.y = class33.y;
                class33_1.z = class33.z;
                class33_1.magnitude = class33.magnitude;
            }
            contrast = k1;
            ambient = i;
        }
    }

    public final void method480(int x, int y, int z) {
        method480(ambient, contrast, x, y, z);
    }

    public final void method480(int intensity, int fall_off, int x, int y, int z) {
        for (int face = 0; face < numberOfTriangleFaces; face++) {
            int triangle_a = face_a[face];
            int triangle_b = face_b[face];
            int triangle_c = face_c[face];
            if (face_render_type == null) {
                int triangle_color = face_color[face] & 0xffff;
                VertexNormal class33 = vertexNormals[triangle_a];
                int light = intensity
                        + (x * class33.x + y * class33.y + z
                        * class33.z)
                        / (fall_off * class33.magnitude);
                face_shade_a[face] = method481(triangle_color, light, 0);

                class33 = vertexNormals[triangle_b];
                light = intensity
                        + (x * class33.x + y * class33.y + z
                        * class33.z)
                        / (fall_off * class33.magnitude);
                face_shade_b[face] = method481(triangle_color, light, 0);

                class33 = vertexNormals[triangle_c];
                light = intensity
                        + (x * class33.x + y * class33.y + z
                        * class33.z)
                        / (fall_off * class33.magnitude);
                face_shade_c[face] = method481(triangle_color, light, 0);
            } else if ((face_render_type[face] & 1) == 0) {
                int triangle_color = face_color[face] & 0xffff;
                int flags = face_render_type[face];
                VertexNormal class33_1 = vertexNormals[triangle_a];
                int light = intensity
                        + (x * class33_1.x + y * class33_1.y + z
                        * class33_1.z)
                        / (fall_off * class33_1.magnitude);
                face_shade_a[face] = method481(triangle_color, light, flags);

                class33_1 = vertexNormals[triangle_b];
                light = intensity
                        + (x * class33_1.x + y * class33_1.y + z
                        * class33_1.z)
                        / (fall_off * class33_1.magnitude);
                face_shade_b[face] = method481(triangle_color, light, flags);

                class33_1 = vertexNormals[triangle_c];
                light = intensity
                        + (x * class33_1.x + y * class33_1.y + z
                        * class33_1.z)
                        / (fall_off * class33_1.magnitude);
                face_shade_c[face] = method481(triangle_color, light, flags);

            }
        }
        vertexNormals = null;
        vertexNormalOffset = null;
        anIntArray1655 = null;
        anIntArray1656 = null;
        if (face_render_type != null) {
            for (int l1 = 0; l1 < numberOfTriangleFaces; l1++)
                if ((face_render_type[l1] & 2) == 2)
                    return;

        }
        face_color = null;
    }

    public final void method482(int mdlYaw, int mdlPitch, int camRoll, int i1, int j1, int k1) {
    	if (boundsType != 2 && boundsType != 1) {
    		normalise();
    	}
        int mdlRoll = 0;// constant parameter
        int midX = Rasterizer.centerX;
        int midY = Rasterizer.centerY;
        int j2 = Rasterizer.SINE[mdlRoll];
        int k2 = Rasterizer.COSINE[mdlRoll];
        int l2 = Rasterizer.SINE[mdlYaw];
        int i3 = Rasterizer.COSINE[mdlYaw];
        int j3 = Rasterizer.SINE[mdlPitch];
        int k3 = Rasterizer.COSINE[mdlPitch];
        int l3 = Rasterizer.SINE[camRoll];
        int i4 = Rasterizer.COSINE[camRoll];
        for (int k4 = 0; k4 < numberOfVerticeCoordinates; k4++) {
            int x = verticesXCoordinate[k4];
            int y = verticesYCoordinate[k4];
            int z = verticesZCoordinate[k4];
            if (mdlPitch != 0) {
                int k5 = y * j3 + x * k3 >> 15;
                y = y * k3 - x * j3 >> 15;
                x = k5;
            }
            if (mdlRoll != 0) {
                int l5 = y * k2 - z * j2 >> 15;
                z = y * j2 + z * k2 >> 15;
                y = l5;
            }
            if (mdlYaw != 0) {
                int i6 = z * l2 + x * i3 >> 15;
                z = z * i3 - x * l2 >> 15;
                x = i6;
            }
            x += i1;
            y += j1;
            z += k1;
            int j6 = y * i4 - z * l3 >> 15;
            z = y * l3 + z * i4 >> 15;
            y = j6;
            projected_vertex_x[k4] = midX + (x << 9) / z;
            projected_vertex_y[k4] = midY + (y << 9) / z;
            projected_vertex_z[k4] = z;
            if (numberOfTexturedFaces > 0) {
                camera_vertex_y[k4] = x;
                camera_vertex_x[k4] = y;
                camera_vertex_z[k4] = z;
            }
        }

        try {
            method483(false, false, 0);
        } catch (Exception _ex) {
        	_ex.printStackTrace();
            /* empty */
        }
    }

    @Override
    public void renderAtPoint(int i, int j, int k, int l, int i1, int j1, int k1,
                              int l1, long i2) {
    	if (boundsType != 1) {
    		calculateDiagonals();
    	}
        int j2 = l1 * i1 - j1 * l >> 15;
        int k2 = k1 * j + j2 * k >> 15;
        int l2 = lowestX * k >> 15;
        int i3 = k2 + l2;
        if (i3 <= 50 || k2 >= 3600)
            return;
        int j3 = l1 * l + j1 * i1 >> 15;
        int k3 = (j3 - lowestX) * Client.gameAreaWidth;
        if (k3 / i3 >= Rasterizer.viewportRight)
            return;
        int l3 = (j3 + lowestX) * Client.gameAreaWidth;
        if (l3 / i3 <= Rasterizer.viewportLeft)
            return;
        int i4 = k1 * k - j2 * j >> 15;
        int j4 = lowestX * j >> 15;
        int k4 = (i4 + j4) * Client.gameAreaWidth;
        if (k4 / i3 <= Rasterizer.viewportTop)
            return;
        int l4 = j4 + (highestY * k >> 15);
        int i5 = (i4 - l4) * Client.gameAreaWidth;
        if (i5 / i3 >= Rasterizer.viewportBottom)
            return;
        int j5 = l2 + (highestY * j >> 15);
        boolean flag = false;
        if (k2 - j5 <= 50)
            flag = true;
        boolean flag1 = false;
        if (i2 > 0 && objectExists) {
            int k5 = k2 - l2;
            if (k5 <= 50)
                k5 = 50;
            if (j3 > 0) {
                k3 /= i3;
                l3 /= k5;
            } else {
                l3 /= i3;
                k3 /= k5;
            }
            if (i4 > 0) {
                i5 /= i3;
                k4 /= k5;
            } else {
                k4 /= i3;
                i5 /= k5;
            }
            int i6 = currentCursorX - Rasterizer.centerX;
            int k6 = currentCursorY - Rasterizer.centerY;
            if (i6 > k3 && i6 < l3 && k6 > i5 && k6 < k4)
                if (rendersWithinOneTile) {
                    objectsInCurrentRegion[objectsRendered++] = i2;
                } else
                    flag1 = true;
        }
        int l5 = Rasterizer.centerX;
        int j6 = Rasterizer.centerY;
        int l6 = 0;
        int i7 = 0;
        if (i != 0) {
            l6 = Rasterizer.SINE[i];
            i7 = Rasterizer.COSINE[i];
        }
        for (int j7 = 0; j7 < numberOfVerticeCoordinates; j7++) {
            int k7 = verticesXCoordinate[j7];
            int l7 = verticesYCoordinate[j7];
            int i8 = verticesZCoordinate[j7];
            if (i != 0) {
                int j8 = i8 * l6 + k7 * i7 >> 15;
                i8 = i8 * i7 - k7 * l6 >> 15;
                k7 = j8;
            }
            k7 += j1;
            l7 += k1;
            i8 += l1;
            int k8 = i8 * l + k7 * i1 >> 15;
            i8 = i8 * i1 - k7 * l >> 15;
            k7 = k8;
            k8 = l7 * k - i8 * j >> 15;
            i8 = l7 * j + i8 * k >> 15;
            l7 = k8;
            projected_vertex_z[j7] = i8;
            if (i8 >= 50) {
                projected_vertex_x[j7] = l5 + (k7 * Client.gameAreaWidth) / i8;
                projected_vertex_y[j7] = j6 + (l7 * Client.gameAreaWidth) / i8;
            } else {
                projected_vertex_x[j7] = -5000;
                flag = true;
            }
            if (flag || numberOfTexturedFaces > 0) {
                camera_vertex_y[j7] = k7;
                camera_vertex_x[j7] = l7;
                camera_vertex_z[j7] = i8;
            }
        }
        try {
            method483(flag, flag1, i2);
        } catch (Exception _ex) {
        	_ex.printStackTrace();
        	/* empty */
        }
    }

	private final void method483(boolean flag, boolean flag1, long i) {
		if (face_render_priorities == null) {
			for (int k = 0; k < numberOfTriangleFaces; k++) {
				method483(flag, flag1, i, k);
			}
		} else {
			for (int priority = 0; priority < 12; priority++) {
				for (int k = 0; k < numberOfTriangleFaces; k++) {
					if (face_render_priorities[k] == priority) {
						method483(flag, flag1, i, k);
					}
				}
			}
		}
	}

	private final void method483(boolean flag, boolean flag1, long i, int k) {
		int l = face_a[k];
		int k1 = face_b[k];
		int j2 = face_c[k];
		int i3 = projected_vertex_x[l];
		int l3 = projected_vertex_x[k1];
		int k4 = projected_vertex_x[j2];
		if (!flag || i3 != -5000 && l3 != -5000 && k4 != -5000) {
			if (flag1 && method486(currentCursorX, currentCursorY, projected_vertex_y[l], projected_vertex_y[k1], projected_vertex_y[j2], i3, l3, k4)) {
				objectsInCurrentRegion[objectsRendered++] = i;
				flag1 = false;
			}
			if ((i3 - l3) * (projected_vertex_y[j2] - projected_vertex_y[k1]) - (projected_vertex_y[l] - projected_vertex_y[k1]) * (k4 - l3) > 0) {
				if (i3 < 0 || l3 < 0 || k4 < 0 || i3 > Rasterizer.endX || l3 > Rasterizer.endX || k4 > Rasterizer.endX)
					hasAnEdgeToRestrict[k] = true;
				else
					hasAnEdgeToRestrict[k] = false;
				rasterize(k);
			}
		}
	}

    private final void rasterize(int face) {
        int a = face_a[face] & 0xffff;
        int b = face_b[face] & 0xffff;
        int c = face_c[face] & 0xffff;
        Rasterizer.restrict_edges = hasAnEdgeToRestrict[face];
        Rasterizer.alpha = face_alpha == null ? 0 : face_alpha[face] & 0xff;
        int face_type;
        if (face_render_type == null) {
            face_type = 0;
        } else // if(face_render_type[face] != -1)
        {
            face_type = face_render_type[face] & 0xff;
        }

        boolean noTextureValues = false;
        if (face_texture == null || face > face_texture.length - 1 || face_texture[face] == -1) {
            noTextureValues = true;
        }

        boolean noCoordinates = false;
        if (texture_coordinates == null || face > texture_coordinates.length - 1 || texture_coordinates[face] == -1) {
            noCoordinates = true;
        }

        if (noTextureValues || noCoordinates) {
            if (face_type == 0) {
                Rasterizer.drawGouraudTriangle(projected_vertex_y[a],
                        projected_vertex_y[b], projected_vertex_y[c],
                        projected_vertex_x[a], projected_vertex_x[b],
                        projected_vertex_x[c], (float) projected_vertex_z[a],
                        (float) projected_vertex_z[b], (float) projected_vertex_z[c], face_shade_a[face], face_shade_b[face], face_shade_c[face]);
                return;
            }
            if (face_type == 1) {
                Rasterizer.drawFlatTriangle(projected_vertex_y[a],
                        projected_vertex_y[b], projected_vertex_y[c],
                        projected_vertex_x[a], projected_vertex_x[b],
                        projected_vertex_x[c], (float) projected_vertex_z[a],
                        (float) projected_vertex_z[b], (float) projected_vertex_z[c], Rasterizer.hsl2rgb[face_shade_a[face]]);
                return;
            }
        }
        if (face_texture != null && face_texture[face] <= 51 || display_model_specific_texture) {
            if (face_type == 0) {
                int texture_type = !noCoordinates ? texture_coordinates[face] & 0xff
                        : -1;
                if (texture_type == 0xff)
                    texture_type = -1;
                if (texture_type == -1 || texture_render_type == null
                        || texture_render_type[texture_type] >= 0) {
                    int x = (texture_type == -1 || texture_render_type[texture_type] > 0) ? a
                            : textured_face_a[texture_type];
                    int y = (texture_type == -1 || texture_render_type[texture_type] > 0) ? b
                            : textured_face_b[texture_type];
                    int z = (texture_type == -1 || texture_render_type[texture_type] > 0) ? c
                            : textured_face_c[texture_type];
                    Rasterizer.drawTexturedTriangle_model(
                            projected_vertex_y[a],
                            projected_vertex_y[b],
                            projected_vertex_y[c],
                            projected_vertex_x[a],
                            projected_vertex_x[b],
                            projected_vertex_x[c],
                            projected_vertex_z[a],
                            projected_vertex_z[b],
                            projected_vertex_z[c],
                            face_shade_a[face], face_shade_b[face],
                            face_shade_c[face], camera_vertex_y[x],
                            camera_vertex_y[y], camera_vertex_y[z],
                            camera_vertex_x[x], camera_vertex_x[y],
                            camera_vertex_x[z], camera_vertex_z[x],
                            camera_vertex_z[y], camera_vertex_z[z], face_texture[face], force_texture
                    );
                    return;
                }
            }

            if (face_type == 1) {
                int texture_type = !noCoordinates ? texture_coordinates[face] & 0xff
                        : -1;
                if (texture_type == 0xff)
                    texture_type = -1;
                if (texture_type == -1 || texture_render_type == null
                        || texture_render_type[texture_type] >= 0) {
                    int x = (texture_type == -1 || texture_render_type[texture_type] > 0) ? a
                            : textured_face_a[texture_type];
                    int y = (texture_type == -1 || texture_render_type[texture_type] > 0) ? b
                            : textured_face_b[texture_type];
                    int z = (texture_type == -1 || texture_render_type[texture_type] > 0) ? c
                            : textured_face_c[texture_type];
                    Rasterizer.drawTexturedTriangle_model(projected_vertex_y[a],
                            projected_vertex_y[b], projected_vertex_y[c],
                            projected_vertex_x[a], projected_vertex_x[b],
                            projected_vertex_x[c], projected_vertex_z[a],
                            projected_vertex_z[b], projected_vertex_z[c],
                            face_shade_a[face], face_shade_a[face],
                            face_shade_a[face], camera_vertex_y[x],
                            camera_vertex_y[y], camera_vertex_y[z],
                            camera_vertex_x[x], camera_vertex_x[y],
                            camera_vertex_x[z], camera_vertex_z[x],
                            camera_vertex_z[y], camera_vertex_z[z], face_texture[face], force_texture
                    );
                    return;
                }
            }

        }

        if (face_type == 0) {
            Rasterizer.drawGouraudTriangle(projected_vertex_y[a],
                    projected_vertex_y[b], projected_vertex_y[c],
                    projected_vertex_x[a], projected_vertex_x[b],
                    projected_vertex_x[c], (float) projected_vertex_z[a],
                    (float) projected_vertex_z[b], (float) projected_vertex_z[c], face_shade_a[face], face_shade_b[face], face_shade_c[face]);
            return;
        }
        if (face_type == 1) {
            Rasterizer.drawFlatTriangle(projected_vertex_y[a],
                    projected_vertex_y[b], projected_vertex_y[c],
                    projected_vertex_x[a], projected_vertex_x[b],
                    projected_vertex_x[c], (float) projected_vertex_z[a],
                    (float) projected_vertex_z[b], (float) projected_vertex_z[c], Rasterizer.hsl2rgb[face_shade_a[face]]);
            return;
        }
    }

    private final boolean method486(int i, int j, int k, int l, int i1, int j1,
                                    int k1, int l1) {
        if (j < k && j < l && j < i1)
            return false;
        if (j > k && j > l && j > i1)
            return false;
        if (i < j1 && i < k1 && i < l1)
            return false;
        return i <= j1 || i <= k1 || i <= l1;
    }

}
