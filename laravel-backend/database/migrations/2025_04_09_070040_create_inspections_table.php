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
        Schema::create('inspections', function (Blueprint $table) {
            $table->id();
            $table->foreignId('vehicle_id')->constrained('vehicles');
            $table->foreignId('center_id')->constrained('centers');
            $table->foreignId('inspector_id')->constrained('users');
            $table->date('inspection_date');
            $table->date('next_inspection_date');
            $table->string('certificate_number', 50)->unique();
            $table->enum('result', ['passed', 'failed', 'conditional'])->nullable();
            $table->text('notes')->nullable();
            $table->decimal('fee', 12, 2);
            $table->integer('mileage')->nullable();
            $table->enum('status', ['pending', 'in_progress', 'completed', 'cancelled'])->default('pending');
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
        Schema::dropIfExists('inspections');
    }
};
