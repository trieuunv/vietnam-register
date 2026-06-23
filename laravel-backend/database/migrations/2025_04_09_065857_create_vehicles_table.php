<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('vehicles', function (Blueprint $table) {
            $table->id();
            $table->foreignId('owner_id')->constrained('owners');
            $table->foreignId('vehicle_type_id')->constrained('vehicle_types');
            $table->string('registration_number', 20);
            $table->string('chassis_number', 50);
            $table->string('engine_number', 50);
            $table->string('brand', 50);
            $table->string('model', 50);
            $table->integer('manufacture_year');
            $table->string('color', 30);
            $table->integer('seat_count');
            $table->date('first_registration_date');
            $table->string('purpose_of_use', 100);
            $table->enum('registration_status', ['registered', 'pending_registration', 'unregistered', 'suspended']);
            $table->enum('status', ['active', 'inactive', 'banned'])->default('active');
            $table->timestamps();
            $table->softDeletes();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('vehicles');
    }
};
